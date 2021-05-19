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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Check whether specified CRUD methods are implemented in a specific REST maturity level 3 controller.
 * The type of the REST method results from the http verb which is contained in the method name.
 * Note that all test methods in this class require the base path "/level-3"
 */
public class GenericControllerTests extends GenericTests {

    private static final String BASE_PATH = "";
    private static final int GET_ALL_TEST_COUNT = 4;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;


    public GenericControllerTests(WebApplicationContext appContext, MockMvc mockMvc, ObjectMapper objectMapper) {
        super(appContext);
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    /**
     * Method: GET
     * @param expectedObject pass an already existing object, pass null if a new one should be created
     * @param objectDescription object description of the object which this action is performed on
     * @param expectedLinks list of links which should be included in the response
     * @param hiddenLinks list of links which should not be included in the response
     * @return the DTO of the entity
     * @throws Exception
     */
    public Object getTest(Object expectedObject, ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        expectedObject = prepareObject(objectDescription, expectedObject);

        ResultActions resultActions = mockMvc
                .perform(get(BASE_PATH + objectDescription.getRestPathLvl3() + "/" + oir.getId(expectedObject)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActions(expectedObject, resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), "");
        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Class<?> dtoClass = Class.forName(objectDescription.getDtoClassPath());
        return objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), dtoClass);
    }

    /**
     * Method: GET ALL
     * @param objectDescription object description of the object which this action is performed on
     * @param expectedLinks list of links which should be included in the response
     * @param hiddenLinks list of links which should not be included in the response
     * @param collectionSelfLink the self link of the collection which should be included in the response
     * @throws Exception
     */
    public void getAllTest(ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks, Link collectionSelfLink) throws Exception {
        // Save Object List
        CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
        List<Object> objectList = objectBuilder.buildObjectList(objectDescription, GET_ALL_TEST_COUNT);
        for (Object object : objectList) {
            repository.save(object);
        }

        // Perform getAll
        ResultActions resultActions = mockMvc
                .perform(get(BASE_PATH + objectDescription.getRestPathLvl3()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Test Fields
        for (int i = 0; i < GET_ALL_TEST_COUNT; i++) {
            String preIdentifier = "._embedded." + objectDescription.getAttributePlural() + "[" + i + "]";
            objectValidator.validateResultActions(objectList.get(i), resultActions, objectDescription.getAttributes(), objectDescription.getHiddenAttributes(), preIdentifier);
            objectValidator.validateResultActionLinks(Collections.singletonList(objectList.get(i)), resultActions, expectedLinks, hiddenLinks, "._embedded." + objectDescription.getAttributePlural() + "[" + i + "]");
        }

        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, new Link[]{collectionSelfLink}, new Link[]{}, "");
    }

    /**
     * Method: POST
     * @param objectDescription object description of the object which this action is performed on
     * @param expectedLinks list of links which should be included in the response
     * @param hiddenLinks list of links which should not be included in the response
     * @return the created object
     * @throws Exception
     */
    public Object postTest(ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        Attribute[] diffAttributes = getAttributeDiff(objectDescription.getAttributes(), objectDescription.getHiddenAttributes());

        Object expectedObject = objectBuilder.buildObject(objectDescription);

        // Perform Post
        ResultActions resultActions = mockMvc
                .perform(
                        post(BASE_PATH + objectDescription.getRestPathLvl3())
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        objectValidator.validateResultActions(expectedObject, resultActions, diffAttributes, objectDescription.getHiddenAttributes(), "");
        objectValidator.validateResultActionLinks(new ArrayList<>(){}, resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, objectDescription.getAttributes());
        return retrievedObject;
    }

    /**
     * Method: PUT
     * @param expectedObject pass an already existing object, pass null if a new one should be created
     * @param objectDescription object description of the object which this action is performed on
     * @param expectedLinks list of links which should be included in the response
     * @param hiddenLinks list of links which should not be included in the response
     * @throws Exception
     */
    public void putTest(Object expectedObject, ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        expectedObject = prepareObject(objectDescription, expectedObject);

        objectBuilder.setObjectFieldValues(expectedObject, objectDescription.getUpdatedAttributes());

        ResultActions resultActions = mockMvc
                .perform(
                        put(BASE_PATH + objectDescription.getRestPathLvl3() + "/" + oir.getId(expectedObject))
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, objectDescription.getAttributes());
    }

    /**
     * Method: PATCH
     * @param expectedObject pass an already existing object, pass null if a new one should be created
     * @param objectDescription object description of the object which this action is performed on
     * @param expectedLinks list of links which should be included in the response
     * @param hiddenLinks list of links which should not be included in the response
     * @throws Exception
     */
    public void patchTest(Object expectedObject, ObjectDescription objectDescription, Link[] expectedLinks, Link[] hiddenLinks) throws Exception {
        expectedObject = prepareObject(objectDescription, expectedObject);

        objectBuilder.setObjectFieldValues(expectedObject, objectDescription.getUpdatedAttributes());

        ResultActions resultActions = mockMvc
                .perform(
                        patch(BASE_PATH + objectDescription.getRestPathLvl3() + "/" + oir.getId(expectedObject))
                                .content(objectMapper.writeValueAsString(expectedObject))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        objectValidator.validateResultActionLinks(Collections.singletonList(expectedObject), resultActions, expectedLinks, hiddenLinks, "");

        Object retrievedObject = oir.getRepository(objectDescription.getClassPath()).findAll().iterator().next();
        objectValidator.validateTwoObjects(expectedObject, retrievedObject, objectDescription.getAttributes());
    }

    /**
     * Method: DELETE
     * @param object pass an already existing object, pass null if a new one should be created
     * @param objectDescription object description of the object which this action is performed on
     * @throws Exception
     */
    public void deleteTest(Object object, ObjectDescription objectDescription) throws Exception {
        object = prepareObject(objectDescription, object);

        // Perform delete
        mockMvc
                .perform(delete(BASE_PATH + objectDescription.getRestPathLvl3() + "/" + oir.getId(object)))
                .andExpect(status().isNoContent());

        Optional<Object> objectOp = oir.getRepository(objectDescription.getClassPath()).findById(oir.getId(object));
        assertFalse(objectOp.isPresent());
    }

    private Object prepareObject(ObjectDescription objectDescription, Object alreadyExistingObject) throws Exception {
        if (alreadyExistingObject == null) {
            CrudRepository<Object, UUID> repository = oir.getRepository(objectDescription.getClassPath());
            alreadyExistingObject = objectBuilder.buildObject(objectDescription);
            repository.save(alreadyExistingObject);
        }

        return alreadyExistingObject;
    }

    private Attribute[] getAttributeDiff(Attribute[] attributes, Attribute[] hiddenAttributes) {
        Set<Attribute> allAttributeSet = new HashSet<>();
        Collections.addAll(allAttributeSet, attributes);
        Set<Attribute> hiddenAttributeSet = new HashSet<>();
        Collections.addAll( hiddenAttributeSet, hiddenAttributes );
        allAttributeSet.removeAll( hiddenAttributeSet );
        return allAttributeSet.toArray(new Attribute[0]);
    }
}
