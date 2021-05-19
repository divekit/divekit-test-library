package thkoeln.st.springtestlib.controller.level3;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import thkoeln.st.springtestlib.core.Attribute;
import thkoeln.st.springtestlib.core.GenericTests;
import thkoeln.st.springtestlib.core.Link;
import thkoeln.st.springtestlib.core.objectdescription.ObjectDescription;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Ensure certain relationships are implemented in a specific REST maturity level 3 controller.
 * The type of the relationship results from the method name.
 * All test methods in this class are used for value objects as children
 * Note that all test methods in this class require the base path "/level-3"
 */
public class GenericControllerAssociationVOTests extends GenericTests {

    private static final String BASE_PATH = "";
    private static final int COLLECTION_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerAssociationVOTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Method: PUT
     * Relationship: one to one
     * @param parentObjectDescription parent object description of the relationship
     * @param childObjectDescription child object description of the relationship
     * @throws Exception
     */
    public Object putOneToOneVOTest(Object parentObject, ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);
        }

        // Create Child
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        ResultActions resultActions = putChild(parentObjectDescription.getRestPathLvl3(), parentObject, childObject, childObjectDescription.getAttributeSingular());

        // Test Fields
        objectValidator.validateResultActions(childObject, resultActions, childObjectDescription.getAttributes(), new Attribute[]{}, "." + childObjectDescription.getAttributeSingular());
        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");

        objectValidator.assertToOneRelation(parentRepository, parentObject, childObject, childObjectDescription.getGetToOne(), false);
        return childObject;
    }

    /**
     * Method: POST
     * Relationship: one to many
     * @param parentObjectDescription parent object description of the relationship
     * @param childObjectDescription child object description of the relationship
     * @throws Exception
     */
    public Object postOneToManyVOTest(Object parentObject, ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);
        }

        // Create Children
        Object childObject = objectBuilder.buildObject(childObjectDescription);

        ResultActions resultActions = postChildToCollection(parentObjectDescription.getRestPathLvl3(), parentObject, childObject, childObjectDescription.getAttributePlural());

        // Test Fields
        String preIdentifier = "." + childObjectDescription.getAttributePlural() + "[0]";
        objectValidator.validateResultActions(childObject, resultActions, childObjectDescription.getAttributes(), new Attribute[]{}, preIdentifier);

        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");
        objectValidator.assertToManyRelation(parentRepository, parentObject, Collections.singletonList(childObject), childObjectDescription.getGetToMany(), false);
        return childObject;
    }

    /**
     * Method: DELETE
     * Relationship: one to many
     * @param parentObjectDescription parent object description of the relationship
     * @param childObjectDescription child object description of the relationship
     * @throws Exception
     */
    public void deleteOneToManyVOTest(Object parentObject, ObjectDescription parentObjectDescription, ObjectDescription childObjectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        // Save Parent
        CrudRepository<Object, UUID> parentRepository = oir.getRepository(parentObjectDescription.getClassPath());
        if (parentObject == null) {
            parentObject = objectBuilder.buildObject(parentObjectDescription);
            parentRepository.save(parentObject);

            List<Object> childObjects = objectBuilder.buildObjectList(childObjectDescription, COLLECTION_COUNT);
            Field childField = parentObject.getClass().getDeclaredField(childObjectDescription.getAttributePlural());
            childField.setAccessible(true);
            childField.set(parentObject, childObjects);
        }

        ResultActions resultActions = mockMvc
            .perform(delete(BASE_PATH + parentObjectDescription.getRestPathLvl3() + "/" + oir.getId(parentObject) + "/" + childObjectDescription.getAttributePlural()))
            .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(parentObject), resultActions, expectedLinks, hiddenLinks, "");
        objectValidator.assertToManyRelation(parentRepository, parentObject, new ArrayList<>() {}, childObjectDescription.getGetToMany(), false);
    }

    private ResultActions putChild(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(put(BASE_PATH + restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private ResultActions postChildToCollection(String restPath, Object parentObject, Object childObject, String childAttributeName) throws Exception {
        return mockMvc
                .perform(post(BASE_PATH + restPath + "/" + oir.getId(parentObject) + "/" + childAttributeName)
                        .content(objectMapper.writeValueAsString(childObject))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
