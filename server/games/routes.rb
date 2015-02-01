
GameMachine::Routes.game_messages do

  # The route id and the to: parameter are both required.  Everything else is optional
  # route id's can be any positive integer.  For ruby actors, to: should be the class.  For java actors
  # to: should be the string name of the actor (as defined in the java class)
  route 1, to: 'Tutorial::ItemManager', distributed: true, name: 'item_manager'

  route 10, to: 'LatencyTest', distributed: false, name: 'LatencyTest'
  route 11, to: 'PathService', distributed: false, name: 'PathService'
  route 12, to: 'UnityProxy', distributed: false, name: 'UnityProxy'
  route 100, to: 'GameMachine::GameSystems::UserMessageTest', distributed: false, name: 'user_message_test'
  route 200, to: 'CharacterHandler', distributed: false, name: 'CharacterHandler'
  route 201, to: 'HarvestHandler', distributed: false, name: 'HarvestHandler'
  route 201, to: 'CraftingHandler', distributed: false, name: 'CraftingHandler'
end