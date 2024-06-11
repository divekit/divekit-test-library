| Requirement #                                                      | URI                               | VERB   |
|--------------------------------------------------------------------|-----------------------------------|--------|
| Get all space ships                                                | /spaceShips                       | Get    |
| Create a new space ship                                            | /spaceShips                       | POST   |
| Get a specific space ship by ID                                    | /spaceShips/{spaceShip-id}        | GET    |
| Give a specific space ship a task                                  | /spaceShips/{spaceShip-id}/tasks  | POST   |
| Get the task history for a specific space ship                     | /spaceShips/{spaceShip-id}/tasks  | GET    |
| Clear the task history for a specific space ship                   | /spaceShips/{spaceShip-id}/tasks  | Delete |
| Delete a specific space ship                                       | /spaceShips/{spaceShip-id}        | DELETE |
| Load or unload some load of mineralic ore on a specific space ship | /spaceShips/{spaceShip-id}/load   | PUT    |
| Get all planets                                                    | /planets                          | get    |
| Get a specific planet by ID                                        | /planets/{planet-id}              | GeT    |
| Replenish mineralic ore on a specific planet                       | /planets/{planet-id}/mineralicOre | PUT    |
| Install a jump gate                                                | /jumpGates                        | POST   |
| Get all jump gates currently installed                             | /jumpGates                        | GET    |
| Get a specific jump gate                                           | /jumpGates/{jumpGate-id}          | GET    |
| Shut down a specific jump gate                                     | /jumpGates/{jumpGate-id}          | DELETE |
| Relocate a specific jump gate to another set of planets            | /jumpGates/{jumpGate-id}          | patch  |

