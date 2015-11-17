using System;

namespace GameMachine {
    public class TimeUtil {

        public static double EpochTime() {
            TimeSpan t = (DateTime.UtcNow - new DateTime(1970, 1, 1));
            return t.TotalSeconds;
        }
    }
}
