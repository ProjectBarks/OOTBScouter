package me.brandonbarker.ftcscouter.match;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = {"m"})
public enum ScoreType {

    GET_OFF_RAMP("Get off ramp", Phase.AUTONOMOUS, new ValidBooleanTest(), false, 20),
    KICKSTAND_RELEASED("Kickstand released", Phase.AUTONOMOUS, new ValidBooleanTest(), false, 30),
    CENTER_GOAL("Center Goal Scored", Phase.AUTONOMOUS, new ValidBooleanTest(), false, 60),
    GOALS_PARKING_ZONE_A("Goals in parking zone in auto", Phase.AUTONOMOUS, new ValidRangeTest(0, 3), 0, 20),
    ROLLING_GOALS("Rolling goals scored", Phase.AUTONOMOUS, new ValidRangeTest(0, 3), 0, 30),
    LOW_GOAL("Low rolling goal", Phase.TELEOP, new ValidRangeTest(0, 100), 0, 0.27),
    MED_GOAL("Medium rolling goal", Phase.TELEOP, new ValidRangeTest(0, 100), 0, 1.14),
    HIGH_GOAL("High rolling goal", Phase.TELEOP, new ValidRangeTest(0, 100), 0, 2.61),
    CENT_GOAL("Center rolling goal", Phase.ENDGAME, new ValidRangeTest(0, 100), 0, 1.8),
    GOALS_PARKING_ZONE_E("Goals in parking zone in endgame", Phase.ENDGAME, new ValidRangeTest(0, 3), 0, 10),
    GOALS_FLOOR("Goals off floor", Phase.ENDGAME, new ValidRangeTest(0, 3), 0, 30),
    ROBOT_PARKING("Robot in parking zone", Phase.ENDGAME, new ValidBooleanTest(), false, 10),
    ROBOT_FLOOR("Robot off floor", Phase.ENDGAME, new ValidBooleanTest(), false, 30);


    @Getter
    private ValidInputTest mInput;
    @Getter
    private String mName;
    @Getter
    private Phase mPhase;
    @Getter
    private Object mFiller;
    private double mPoints;

    ScoreType(String name, Phase phase, ValidInputTest inputTester, Object filler, double pointValue) {
        this.mName = name;
        this.mPhase = phase;
        this.mInput = inputTester;
        this.mPoints = pointValue;
        this.mFiller = filler;
    }

    public double getScore(Object value) {
        value = value.getClass().equals(Integer.TYPE) ? (Integer)value : value;
        //noinspection ConstantConditions
        value = value.getClass().equals(Boolean.TYPE) ? (Boolean)value : value;
        value = value instanceof Number ? ((Number) value).intValue() : value;
        if (!value.getClass().isPrimitive()) {
            if (value instanceof Boolean) {
                return ((Boolean)value) ? this.mPoints : 0;
            } else if (value instanceof Integer) {
                return this.mPoints * (Integer) value;
            }
        }
        return 0;
    }

    public boolean isValid(Object input) {
        return this.mInput.isValid(input);
    }

    public enum Phase {

        AUTONOMOUS("Auto"),
        TELEOP("Tele-Op"),
        ENDGAME("End Game");

        @Getter
        private String mName;

        Phase(String name) {
            this.mName = name;
        }
    }

    public static class ValidBooleanTest extends ValidInputTest {

        public ValidBooleanTest() {
            super(Boolean.class);
        }

        @Override
        protected boolean isValidSecondary(Object input) {
            return true;
        }
    }

    public static class ValidRangeTest extends ValidInputTest {
        @Getter
        private int mMax;
        @Getter
        private int mMin;

        public ValidRangeTest(int min, int max) {
            super(Integer.class);
            this.mMin = min;
            this.mMax = max;
        }

        @Override
        protected boolean isValidSecondary(Object input) {
            Integer intValue = (Integer) input;
            return (this.mMin <= intValue) && (intValue <= this.mMax);
        }
    }
}
