
# This class is set in the config.  Every time the client sends it's location
# the verify method is called.  You can use this to prevent things like speed hacking.
module Example
  class TrackingHandler

    # This method must return true for the players location to be saved.
    # If you return false here, the location sent by the player will be discarded
    def verify(entity)
      #vector = entity.vector3
      #player_id = entity.id
      #entity_type = entity.entity_type
      #track_extra = entity.track_entity.track_extra
      true
    end
  end
end
