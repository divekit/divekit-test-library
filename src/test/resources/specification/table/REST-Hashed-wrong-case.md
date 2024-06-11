| Requirement #                                                      | URI                                 | VERB   |
|--------------------------------------------------------------------|-------------------------------------|--------|
| Get all space ships                                                | /spaceShips                         | GET    |
| Create a new space ship                                            | /spaceShips                         | POST   |
| Get a specific space ship by ID                                    | /spaceships/{spaceShip-id}          | GET    |
| Give a specific space ship a task                                  | /spaceShips/{spaceShip-id}/tasks    | POST   |
| Get the task history for a specific space ship                     | /spaceShips/{spaceShip-id}/tasks    | GET    |
| Clear the task history for a specific space ship                   | /spaceShips/{spaceShip-id}/tasks    | DELETE |
| Delete a specific space ship                                       | /spaceShips/{spaceShip-id}          | DELETE |
| Load or unload some load of mineralic ore on a specific space ship | /spaceShips/{spaceShip-id}/load     | PUT    |
| Get all planets                                                    | /planets                            | GET    |
| Get a specific planet by ID                                        | /planets/{planet-id}                | GET    |
| Replenish mineralic ore on a specific planet                       | /planets/{planet-id}/mineralicOre   | PUT    |
| Install a jump gate                                                | /jumpGates                          | POST   |
| Get all jump gates currently installed                             | /jumpGates                          | GET    |
| Get a specific jump gate                                           | /jumpGates/{jumpGate-id}            | GET    |
| Shut down a specific jump gate                                     | /jumpGates/{jumpGate-id}            | DELETE |
| Relocate a specific jump gate to another set of planets            | /jumpGates/{jumpGate-id}            | PATCH  |

