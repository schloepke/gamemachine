
# If you have things you want loaded before game machine starts up, this
# is the place to do it.  Examples might be specific handlers, or loading up
# static data that needs to be there before the rest of the system starts up.


# This needs to be loaded before the entity tracking system starts
require_relative 'example/lib/tracking_handler'
