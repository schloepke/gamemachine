
# If you have things you want loaded before game machine starts up, this
# is the place to do it.  Examples might be specific handlers, or loading up
# static data that needs to be there before the rest of the system starts up.

# json models that are global accross all games/plugins
require_relative 'models'

# Plugins that extend or work with core Game Machine features
require_relative 'plugins'

# This needs to be loaded before the entity tracking system starts
require_relative 'example/lib/tracking_handler'

