package io.opentimeline

import io.opentimeline.opentime.IsDropFrameRate
import io.opentimeline.opentime.RationalTime
import io.opentimeline.opentime.exception.InvalidRateForDropFrameTimecodeException
import io.opentimeline.opentime.exception.InvalidTimecodeRateException
import spock.lang.Specification

import javax.script.ScriptEngineManager

import static spock.util.matcher.HamcrestMatchers.closeTo

class RationalTimeTest extends Specification {

    def "Create an empty RationalTime"() {
        when: "A RationalTime is created using the no-arg constructor"
        def t = new RationalTime()
        then: "The RationalTime has value: 0 and rate: 1"
        verifyAll {
            t.getValue() == 0
            t.getRate() == 1
        }
    }

    def "Create a RationalTime"() {
        when: "A RationalTime is created using the parameterized constructor with parameters (value: 30, rate: 1)"
        def t = new RationalTime(30, 1)
        then: "The RationalTime has value: 30 and rate: 1"
        verifyAll {
            t.getValue() == 30
            t.getRate() == 1
        }
    }

    def "Create another RationalTime"() {
        when: "A RationalTime is created using the builder using only a value of 32"
        def t = new RationalTime.RationalTimeBuilder().setValue(32).build()
        then: "The RationalTime has value: 32 and rate: 1"
        verifyAll {
            t.getValue() == 32
            t.getRate() == 1
        }
    }

    def "Create yet another RationalTime"() {
        when: "A RationalTime is created using the builder using only a rate of 29.97"
        def t = new RationalTime.RationalTimeBuilder().setRate(29.97).build()
        then: "The RationalTime has value: 0 and rate: 29.97"
        verifyAll {
            t.getValue() == 0
            t.getRate() == 29.97d
        }
    }

    def "Serialize a RationalTime to String"() {
        given: "A RationalTime with value: 10, rate: 24"
        def t = new RationalTime(10, 24)
        when: "toString() is called on the RationalTime object"
        def serializedRationalTime = t.toString()
        then: "The serialized RationalTime contains the appropriate values: io.opentimeline.opentime.RationalTime(value=10.0, rate=24.0)"
        verifyAll {
            serializedRationalTime == "io.opentimeline.opentime.RationalTime(value=10.0, rate=24.0)"
        }
    }

    def "Check RationalTime equality"() {
        given: "Two RationalTime objects with the same values"
        def t1 = new RationalTime.RationalTimeBuilder().setValue(30.2).build()
        def t2 = new RationalTime.RationalTimeBuilder().setValue(30.2).build()
        when: "Equality is checked using the equals() method"
        def equals = t1.equals(t2)
        then: "Both RationalTimes are equal"
        verifyAll {
            equals
        }
    }

    def "Check RationalTime inequality"() {
        given: "Two RationalTime objects with the different values"
        def t1 = new RationalTime.RationalTimeBuilder().setValue(30.2).build()
        def t2 = new RationalTime.RationalTimeBuilder().setValue(33.2).build()
        when: "Equality is checked using the equals() method"
        def equals = t1.equals(t2)
        then: "Both RationalTimes are not equal"
        verifyAll {
            !equals
        }
    }

    def "RationalTime comparison"() {
        given: "Two RationalTime objects"
        def t1 = new RationalTime(t1Value, t1Rate)
        def t2 = new RationalTime(t2Value, t2Rate)
        when: "RationalTimes are compared using compareTo() method"
        def comparison = t1.compareTo(t2)
        then: "Value returned is positive when lhs > rhs, negative when lhs < rhs, 0 when lhs = rhs"
        verifyAll {
            comparison == valueReturned
        }
        where:

        description                          | t1Value | t1Rate | t2Value | t2Rate | valueReturned
        "t1 < t2"                            | 15.2d   | 1.0d   | 15.6d   | 1.0d   | -1
        "t1 > t2"                            | 15.6d   | 1.0d   | 15.2d   | 1.0d   | 1
        "t1 > t2 (implicit base conversion)" | 15.6d   | 1.0d   | 15.6d   | 48.0d  | 1
        "t1 = t2"                            | 15.2d   | 1.0d   | 15.2d   | 1.0d   | 0
        "t1 = t2 (implicit base conversion)" | 15.2d   | 1.0d   | 30.4d   | 2.0d   | 0
    }

    def "Base Conversion using new rate"() {
        given: "A RationalTime"
        def t = new RationalTime(10, 24)
        when: "The RationalTime is rescaled to a new rate"
        def t2 = t.rescaledTo(48)
        then: "The rescaled RationalTime has the new rate"
        verifyAll {
            t2.getRate() == 48
        }
    }

    def "Base Conversion using a RationalTime"() {
        given: "A RationalTime"
        def t = new RationalTime(10, 24)
        when: "The RationalTime is rescaled to the rate of another RationalTime"
        def t2 = new RationalTime(20, 48)
        def t3 = t.rescaledTo(t2)
        then: "The rescaled RationalTime has the rate of the provided RationalTime"
        verifyAll {
            t3.getRate() == t2.getRate()
        }
    }

    def "RationalTime to timecode round trip"() {
        given: "A timecode: 00:06:56:17"
        def timecode = "00:06:56:17"
        when: "The timecode is converted to a RationalTime, at a rate of 24 fps"
        def t = RationalTime.fromTimecode(timecode, 24)
        and: "The RationalTime is converted back to timecode"
        def roundTripTimecode = t.toTimecode()
        then: "Both timecodes match"
        verifyAll {
            timecode == roundTripTimecode
        }
    }

    def "Timecode to RationalTime conversion at 24 fps"() {
        when: "The timecode is converted to a RationalTime"
        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = engine.eval(RationalTime_value)
        def t = new RationalTime(value as double, RationalTime_rate)
        then:
        verifyAll {
            t.equals(RationalTime.fromTimecode(timecode, RationalTime_rate))
        }

        where:

        description                                | timecode      | RationalTime_value      | RationalTime_rate
        "0 seconds"                                | "00:00:00:00" | "0"                     | 24d
        "1 second"                                 | "00:00:01:00" | "24"                    | 24d
        "1 minute"                                 | "00:01:00:00" | "24 * 60"               | 24d
        "1 hour"                                   | "01:00:00:00" | "24 * 60 * 60"          | 24d
        "24 hours"                                 | "24:00:00:00" | "24 * 60 * 60 * 24"     | 24d
        "23 hours 50 minutes 59 seconds 23 frames" | "23:59:59:23" | "24 * 60 * 60 * 24 - 1" | 24d
    }

    def "Timecode to RationalTime conversion at 23.967 fps"() {
        when: "The timecode is converted to a RationalTime"
        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = engine.eval(RationalTime_value) as double
        def rate = engine.eval(RationalTime_rate) as double
        def t = new RationalTime(value, rate)
        then:
        verifyAll {
            t.equals(RationalTime.fromTimecode(timecode, rate))
        }

        where:

        description                                | timecode      | RationalTime_value      | RationalTime_rate
        "0 seconds"                                | "00:00:00:00" | "0"                     | "23.976"
        "1 second"                                 | "00:00:01:00" | "24"                    | "23.976"
        "1 minute"                                 | "00:01:00:00" | "24 * 60"               | "23.976"
        "1 hour"                                   | "01:00:00:00" | "24 * 60 * 60"          | "23.976"
        "24 hours"                                 | "24:00:00:00" | "24 * 60 * 60 * 24"     | "23.976"
        "23 hours 50 minutes 59 seconds 23 frames" | "23:59:59:23" | "24 * 60 * 60 * 24 - 1" | "24000.0 / 1001.0"
    }

    def "Timecode to RationalTime conversion at 29.97 fps (drop frame rate)"() {
        /**
         Test drop frame in action. Focused on minute roll overs
         We nominal_fps 30 for frame calculation
         For this frame rate we drop 2 frames per minute except every 10th.

         Compensation is calculated like this when below 10 minutes:
         (fps * seconds + frames - dropframes * (minutes - 1))
         Like this when not a whole 10 minute above 10 minutes:
         --minutes == minutes - 1
         (fps * seconds + frames - dropframes * (--minutes - --minutes / 10))
         And like this after that:
         (fps * seconds + frames - dropframes * (minutes - minutes / 10))
         */
        when: "The timecode is converted to a RationalTime"
        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = engine.eval(RationalTime_value) as double
        def rate = engine.eval(RationalTime_rate) as double
        def t = new RationalTime(value, rate)
        then:
        verifyAll {
            t.equals(RationalTime.fromTimecode(timecode, rate))
            timecode == t.toTimecode(29.97, IsDropFrameRate.ForceYes)
        }

        where:

        description               | timecode      | RationalTime_value                                 | RationalTime_rate
        "First four frames"       | "00:00:00;00" | "0"                                                | "29.97"
        "First four frames"       | "00:00:00;01" | "1"                                                | "29.97"
        "First four frames"       | "00:00:00;02" | "2"                                                | "29.97"
        "First four frames"       | "00:00:00;03" | "3"                                                | "29.97"
        "First minute rollover"   | "00:00:59;29" | "30 * 59 + 29"                                     | "29.97"
        "First minute rollover"   | "00:01:00;02" | "30 * 59 + 30"                                     | "29.97"
        "First minute rollover"   | "00:01:00;03" | "30 * 59 + 31"                                     | "29.97"
        "First minute rollover"   | "00:01:00;04" | "30 * 59 + 32"                                     | "29.97"
        "First minute rollover"   | "00:01:00;05" | "30 * 59 + 33"                                     | "29.97"
        "Fifth minute"            | "00:04:59;29" | "30 * 299 + 29 - 2 * 4"                            | "29.97"
        "Fifth minute"            | "00:05:00;02" | "30 * 299 + 30 - 2 * 4"                            | "29.97"
        "Fifth minute"            | "00:05:00;03" | "30 * 299 + 31 - 2 * 4"                            | "29.97"
        "Fifth minute"            | "00:05:00;04" | "30 * 299 + 32 - 2 * 4"                            | "29.97"
        "Fifth minute"            | "00:05:00;05" | "30 * 299 + 33 - 2 * 4"                            | "29.97"
        "Seventh minute"          | "00:06:59;29" | "30 * 419 + 29 - 2 * 6"                            | "29.97"
        "Seventh minute"          | "00:07:00;02" | "30 * 419 + 30 - 2 * 6"                            | "29.97"
        "Seventh minute"          | "00:07:00;03" | "30 * 419 + 31 - 2 * 6"                            | "29.97"
        "Seventh minute"          | "00:07:00;04" | "30 * 419 + 32 - 2 * 6"                            | "29.97"
        "Seventh minute"          | "00:07:00;05" | "30 * 419 + 33 - 2 * 6"                            | "29.97"
        "Tenth minute"            | "00:09:59;29" | "30 * 599 + 29 - 2 * (10 - Math.floor(10/10))"     | "29.97"
        "Tenth minute"            | "00:10:00;00" | "30 * 599 + 30 - 2 * (10 - Math.floor(10/10))"     | "29.97"
        "Tenth minute"            | "00:10:00;01" | "30 * 599 + 31 - 2 * (10 - Math.floor(10/10))"     | "29.97"
        "Tenth minute"            | "00:10:00;02" | "30 * 599 + 32 - 2 * (10 - Math.floor(10/10))"     | "29.97"
        "Tenth minute"            | "00:10:00;03" | "30 * 599 + 33 - 2 * (10 - Math.floor(10/10))"     | "29.97"
        "Second hour"             | "01:59:59;29" | "30 * 7199 + 29 - 2 * (120 - Math.floor(120/10))"  | "29.97"
        "Second hour"             | "02:00:00;00" | "30 * 7199 + 30 - 2 * (120 - Math.floor(120/10))"  | "29.97"
        "Second hour"             | "02:00:00;01" | "30 * 7199 + 31 - 2 * (120 - Math.floor(120/10))"  | "29.97"
        "Second hour"             | "02:00:00;02" | "30 * 7199 + 32 - 2 * (120 - Math.floor(120/10))"  | "29.97"
        "Second hour"             | "02:00:00;03" | "30 * 7199 + 33 - 2 * (120 - Math.floor(120/10))"  | "29.97"
        "Second and half hour"    | "02:29:59;29" | "30 * 8999 + 29 - 2 * (150 - Math.floor(150/10))"  | "29.97"
        "Second and half hour"    | "02:30:00;00" | "30 * 8999 + 30 - 2 * (150 - Math.floor(150/10))"  | "29.97"
        "Second and half hour"    | "02:30:00;01" | "30 * 8999 + 31 - 2 * (150 - Math.floor(150/10))"  | "29.97"
        "Second and half hour"    | "02:30:00;02" | "30 * 8999 + 32 - 2 * (150 - Math.floor(150/10))"  | "29.97"
        "Second and half hour"    | "02:30:00;03" | "30 * 8999 + 33 - 2 * (150 - Math.floor(150/10))"  | "29.97"
        "Tenth hour"              | "09:59:59;29" | "30 * 35999 + 29 - 2 * (600 - Math.floor(600/10))" | "29.97"
        "Tenth hour"              | "10:00:00;00" | "30 * 35999 + 30 - 2 * (600 - Math.floor(600/10))" | "29.97"
        "Tenth hour"              | "10:00:00;01" | "30 * 35999 + 31 - 2 * (600 - Math.floor(600/10))" | "29.97"
        "Tenth hour"              | "10:00:00;02" | "30 * 35999 + 32 - 2 * (600 - Math.floor(600/10))" | "29.97"
        "Tenth hour"              | "10:00:00;03" | "30 * 35999 + 33 - 2 * (600 - Math.floor(600/10))" | "29.97"
        /* Since 3 minutes < 10, we subtract 1 from 603 minutes */
        "Tenth hour third minute" | "10:02:59;29" | "30 * 36179 + 29 - 2 * (602 - Math.floor(602/10))" | "29.97"
        "Tenth hour third minute" | "10:03:00;02" | "30 * 36179 + 30 - 2 * (602 - Math.floor(602/10))" | "29.97"
        "Tenth hour third minute" | "10:03:00;03" | "30 * 36179 + 31 - 2 * (602 - Math.floor(602/10))" | "29.97"
        "Tenth hour third minute" | "10:03:00;04" | "30 * 36179 + 32 - 2 * (602 - Math.floor(602/10))" | "29.97"
        "Tenth hour third minute" | "10:03:00;05" | "30 * 36179 + 33 - 2 * (602 - Math.floor(602/10))" | "29.97"
    }

    def "RationalTime to timecode conversion at NTSC 29.97 fps"() {
        when: "The timecode is converted to a RationalTime"
        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = RationalTime_value
        def rate = engine.eval(RationalTime_rate) as double
        def t = new RationalTime(value, rate)
        then:
        verifyAll {
            timecode == t.toTimecode(rate, isDropFrameRate)
        }

        where:

        timecode      | RationalTime_value | RationalTime_rate  | isDropFrameRate
        "10:03:00;05" | 1084319d           | "30000.0 / 1001.0" | IsDropFrameRate.ForceYes
        "10:02:23:29" | 1084319d           | "30000.0 / 1001.0" | IsDropFrameRate.ForceNo
        "10:03:00;05" | 1084319d           | "30000.0 / 1001.0" | IsDropFrameRate.InferFromRate
    }

    def "RationalTime to timecode conversion at NTSC 29.97 fps with invalid rate for drop frame"() {
        when: "The timecode is converted to a RationalTime"
        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = RationalTime_value
        def rate = engine.eval(RationalTime_rate) as double
        def t = new RationalTime(value, rate)
        t.toTimecode(rate, isDropFrameRate)
        then:
        def e = thrown(InvalidRateForDropFrameTimecodeException)
        verifyAll {
            e.message == "rate is not valid for drop frame timecode"
        }

        where:

        RationalTime_value | RationalTime_rate  | isDropFrameRate
        30d                | "24000.0 / 1001.0" | IsDropFrameRate.ForceYes
    }

    def "RationalTime to timecode conversion at 29.97 fps"() {
        given: "A RationalTime"
        def t = new RationalTime(RationalTime_value, 29.97)
        when: "The RationalTime is converted to timecode"
        def toDfTc = t.toTimecode(29.97, IsDropFrameRate.ForceYes)
        def toTc = t.toTimecode(29.97, IsDropFrameRate.ForceNo)
        def toAutoTc = t.toTimecode(29.97, IsDropFrameRate.InferFromRate)
        then: "At 29.97 fps, it is auto-detected as drop frame timecode"
        verifyAll {
            toDfTc == toAutoTc

            /* check calculated against reference */
            toDfTc == dropFrameTimecode
            toTc == timecode

            /* check they convert back */
            RationalTime.fromTimecode(toDfTc, 29.97) == t
            RationalTime.fromTimecode(toTc, 29.97) == t
        }

        where:

        timecode      | dropFrameTimecode | RationalTime_value
        "00:05:59:19" | "00:05:59;29"     | 10789d
        "00:05:59:20" | "00:06:00;02"     | 10790d
        "00:09:59:11" | "00:09:59;29"     | 17981d
        "00:09:59:12" | "00:10:00;00"     | 17982d
        "00:09:59:13" | "00:10:00;01"     | 17983d
        "00:09:59:14" | "00:10:00;02"     | 17984d
    }

    def "Invalid rate to timecode"() {
        given: "A RationalTime with invalid rate: 29.98fps"
        def t = new RationalTime(100, 29.98)

        when: "The RationalTime is converted to timecode"
        t.toTimecode()

        then: "InvalidTimecodeRateException is thrown"
        def e = thrown(InvalidTimecodeRateException)
        verifyAll {
            e.message == "invalid timecode rate"
        }
    }

    def "Convert timestring to RationalTime at 24 fps"() {
        given: "A timestring"
        when: "The timestring is converted to RationalTime"
        def timeObj = RationalTime.fromTimeString(timeString, RationalTime_rate)

        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = engine.eval(RationalTime_value) as double
        def t = new RationalTime(value, RationalTime_rate)

        then: "The value and rate of the RationalTime are as shown below"
        verifyAll {
            t.almostEqual(timeObj, 0.001)
            timeObj.getRate() == RationalTime_rate
        }
        where:

        timeString        || RationalTime_value      | RationalTime_rate
        "00:00:00.041667" || "1"                     | 24d
        "00:00:01"        || "24"                    | 24d
        "00:01:00"        || "24 * 60"               | 24d
        "01:00:00"        || "24 * 60 * 60"          | 24d
        "24:00:00"        || "24 * 60 * 60 * 24"     | 24d
        "23:59:59.958333" || "24 * 60 * 60 * 24 - 1" | 24d
    }

    def "Convert timestring to RationalTime at 25 fps"() {
        given: "A timestring"
        when: "The timestring is converted to RationalTime"
        def timeObj = RationalTime.fromTimeString(timeString, RationalTime_rate)

        def manager = new ScriptEngineManager()
        def engine = manager.getEngineByName("JavaScript")
        def value = engine.eval(RationalTime_value) as double
        def t = new RationalTime(value, RationalTime_rate)

        then: "The value and rate of the RationalTime are as shown below"
        verifyAll {
            t.almostEqual(timeObj, 0.001)
            timeObj.getRate() == RationalTime_rate
        }
        where:

        timeString    || RationalTime_value      | RationalTime_rate
        "00:00:01"    || "25"                    | 25d
        "00:01:00"    || "25 * 60"               | 25d
        "01:00:00"    || "25 * 60 * 60"          | 25d
        "24:00:00"    || "25 * 60 * 60 * 24"     | 25d
        "23:59:59.92" || "25 * 60 * 60 * 24 - 2" | 25d
    }

    def "Convert negative RationalTime to timestring"() {
        given: "A negative RationalTime. value: -24, rate: 24"
        def t = new RationalTime(-24, 24)

        when: "The negative RationalTime is converted to a timestring"
        def resultTimeString = t.toTimeString()

        then: " A valid timestring with negative signage is returned"
        verifyAll {
            resultTimeString == "-00:00:01.0"
        }
    }

    def "Convert zero RationalTime to timestring"() {
        given: "A zero RationalTime."
        def t = new RationalTime()

        when: "The zero RationalTime is converted to a timestring"
        def resultTimeString = t.toTimeString()

        then: " A valid zero timestring is returned"
        def zeroTimeString = "00:00:00.0"
        verifyAll {
            resultTimeString == zeroTimeString
            t.almostEqual(RationalTime.fromTimeString(zeroTimeString, 24), 0.001)
        }
    }

    def "Timestring with microseconds starting with 0"() {
        given: "A RationalTime"
        def t = new RationalTime(2090, 24)

        when: "The RationalTime is converted to timestring and converted back to a RationalTime"
        def compareRt = RationalTime.fromTimeString(t.toTimeString(), 24)

        then: "The round tripped RationalTimes are almost equal"
        verifyAll {
            t.almostEqual(compareRt, 0.001)
        }
    }

    def "Add RationalTimes for a long running timestring at 24 fps"() {
        given: "A timestring: 23:59:59.958333 and corresponding frame number: 24 * 60 * 60 * 24 - 1"
        def timestring = "23:59:59.958333"
        def finalFrameNumber = 24 * 60 * 60 * 24 - 1
        def finalTime = RationalTime.fromFrames(finalFrameNumber, 24)
        verifyAll {
            finalTime.toTimeString() == timestring
        }

        when: "The corresponding RationalTime is constructed by adding step times of 1 frame"
        def cumulativeRationalTime = new RationalTime()
        def stepRationalTime = new RationalTime(1, 24)
        for (int i in 1..finalFrameNumber) {
            cumulativeRationalTime = cumulativeRationalTime.add(stepRationalTime)
        }

        then: "The cumulative time is equal to RationalTime corresponding to the given timecode"
        verifyAll {
            cumulativeRationalTime.almostEqual(finalTime, 0.001)
        }
    }

    def "Construct RationalTimes at step-sizes which are a non-multiple of 24 for a long running timestring at 24 fps"() {
        given: "A timestring: 23:59:59.958333 and corresponding frame number: 24 * 60 * 60 * 24 - 1"
        def timestring = "23:59:59.958333"
        def finalFrameNumber = 24 * 60 * 60 * 24 - 1
        def finalTime = RationalTime.fromFrames(finalFrameNumber, 24)
        verifyAll {
            finalTime.toTimeString() == timestring
        }

        when: "RationalTimes are constructed at a step which is non-multiple of 24"
        def rationalTimes = new ArrayList<RationalTime>()
        for (int fnum = 1113; fnum < finalFrameNumber; fnum += 1113) {
            def rt = RationalTime.fromFrames(fnum, 24)
            rationalTimes.add(rt)
        }

        then: "The constructed RationalTimes successfully round trip the conversion to timestring"
        for (rationalTime in rationalTimes) {
            def ts = rationalTime.toTimeString()
            def t = RationalTime.fromTimeString(ts, 24)
            verifyAll {
                rationalTime.equals(t)
                ts == t.toTimeString()
            }
        }
    }

    def "Convert RationalTime to timestring at 23.976 fps"() {
        given: "A RationalTime at 600 fps"
        def t = new RationalTime(RationalTime_value, 600)
        when: "The RationalTime is converted to a timestring and round-tripped to RationalTime at 23.976 fps"
        def ts = t.toTimeString()
        def t2 = RationalTime.fromTimeString(ts, 23.976)
        then: "The RationalTime at 600fps and the one at 23.976 fps are almost equal"
        verifyAll {
            ts == timestring
            t.almostEqual(t2, 0.001)
        }
        where:

        RationalTime_value || timestring
        1025d              || "00:00:01.708333"
        179900d            || "00:04:59.833333"
        180000d            || "00:05:00.0"
        360000d            || "00:10:00.0"
        720000d            || "00:20:00.0"
        1079300d           || "00:29:58.833333"
        1080000d           || "00:30:00.0"
        1080150d           || "00:30:00.25"
        1440000d           || "00:40:00.0"
        1800000d           || "00:50:00.0"
        1978750d           || "00:54:57.916666"
        1980000d           || "00:55:00.0"
        46700d             || "00:01:17.833333"
        225950d            || "00:06:16.583333"
        436400d            || "00:12:07.333333"
        703350d            || "00:19:32.25"
    }

    def "Add RationalTimes"() {
        given: "10 RationalTimes with values from 1 to 10"
        List<RationalTime> rationalTimes = new ArrayList<>()
        for (int i in 1..10) {
            rationalTimes.add(new RationalTime(i, 24))
        }

        when: "All the RationalTimes are added"
        def sumRationalTime = new RationalTime()
        for (RationalTime rationalTime in rationalTimes) {
            sumRationalTime = sumRationalTime.add(rationalTime)
        }

        then: "The values of all RationalTimes are added correctly. (Expected value: 55, rate: 24)"
        verifyAll {
            sumRationalTime.equals(new RationalTime(55, 24))
        }
    }

    def "Add RationalTimes for a long running timecode"() {
        given: "A timecode: 23:59:59:23 and corresponding frame number: 24 * 60 * 60 * 24 - 1"
        def timecode = "23:59:59:23"
        def finalFrameNumber = 24 * 60 * 60 * 24 - 1
        def finalTime = RationalTime.fromFrames(finalFrameNumber, 24)
        verifyAll {
            finalTime.toTimecode() == timecode
        }

        when: "The corresponding RationalTime is constructed by adding step times of 1 frame"
        def cumulativeRationalTime = new RationalTime()
        def stepRationalTime = new RationalTime(1, 24)
        for (int i in 1..finalFrameNumber) {
            cumulativeRationalTime = cumulativeRationalTime.add(stepRationalTime)
        }

        then: "The cumulative time is equal to RationalTime corresponding to the given timecode"
        verifyAll {
            cumulativeRationalTime.equals(finalTime)
        }
    }

    def "Construct RationalTimes at step-sizes which are a non-multiple of 24 for a long running timecode at 24 fps"() {
        given: "A timecode: 23:59:59:23 and corresponding frame number: 24 * 60 * 60 * 24 - 1"
        def timecode = "23:59:59:23"
        def finalFrameNumber = 24 * 60 * 60 * 24 - 1
        def finalTime = RationalTime.fromFrames(finalFrameNumber, 24)
        verifyAll {
            finalTime.toTimecode() == timecode
        }

        when: "RationalTimes are constructed at a step which is non-multiple of 24"
        def rationalTimes = new ArrayList<RationalTime>()
        for (int fnum = 1113; fnum < finalFrameNumber; fnum += 1113) {
            def rt = RationalTime.fromFrames(fnum, 24)
            rationalTimes.add(rt)
        }

        then: "The constructed RationalTimes successfully round trip the conversion to timestring"
        for (rationalTime in rationalTimes) {
            def tc = rationalTime.toTimecode()
            def t = RationalTime.fromTimecode(tc, 24)
            verifyAll {
                rationalTime.equals(t)
                tc == t.toTimecode()
            }
        }
    }

    def "Construct RationalTime from frames with integer fps"() {
        given: "An integer fps"

        when: "A RationalTime is constructed from a frame number using the integer fps"
        def t1 = RationalTime.fromFrames(101, fps)

        then: "The RationalTime is equivalent to the one initialized using the constructor"
        def t2 = new RationalTime(101, fps)
        verifyAll {
            t1.equals(t2)
        }

        where:

        fps << [25, 30, 48, 60]
    }

    def "Construct RationalTime from frames with non-integer fps"() {
        given: "A non-integer fps"

        when: "A RationalTime is constructed from a frame number using the non-integer fps"
        def t1 = RationalTime.fromFrames(101, fps)

        then: "The RationalTime is equivalent to the one initialized using the constructor"
        def t2 = new RationalTime(101, fps)
        verifyAll {
            t1.equals(t2)
        }

        where:

        fps << [23.98f, 29.97f, 59.94f]
    }

    def "Create RationalTime from seconds"() {
        given: "Seconds"

        when: "A RationalTime is constructed from seconds and round-tripped to seconds"
        def t = RationalTime.fromSeconds(seconds)

        then: "The seconds provided and the round-tripped seconds are equal"
        def roundTripSeconds = t.toSeconds()
        roundTripSeconds closeTo(seconds, 0.00000001)

        where:

        seconds << [1834f, 248474.345f, 3459f / 24f]
    }

    def "Create duration RationalTime from a startTime and endTime"() {
        given: "A start time and end time"
        def startTime = RationalTime.fromFrames(startTime_frames, startTime_rate)
        def endTime = RationalTime.fromFrames(endTime_frames, endTime_rate)

        when: "A duration RationalTime is created from the start time and end time"
        def duration = RationalTime.durationFromStartEndTime(startTime, endTime)

        then: "The duration values are as shown below"

        verifyAll {
            duration.equals(RationalTime.fromFrames(duration_frames, duration_rate))
        }

        where:

        startTime_frames | startTime_rate | endTime_frames | endTime_rate || duration_frames | duration_rate
        100              | 24             | 200            | 24            | 100             | 24
        0                | 1              | 200            | 24            | 200             | 24
    }

    def "Test RationalTime math"() {
        given: "A few RationalTimes"
        def a = RationalTime.fromFrames(100, 24);
        def gap = RationalTime.fromFrames(50, 24);
        def b = RationalTime.fromFrames(150, 24);

        when: "The RationalTimes are added and subtracted amongst themselves"

        then: "The arithmetic operations work as expected"

        verifyAll {
            b.subtract(a).equals(gap)
            a.add(gap).equals(b)
            b.subtract(gap).equals(a)
        }
    }

    def "Test RationalTime math with different scales"() {
        given: "A few RationalTimes"
        def a = RationalTime.fromFrames(100, 24);
        def gap = RationalTime.fromFrames(100, 48);
        def b = RationalTime.fromFrames(75, 12);

        when: "The RationalTimes are added and subtracted amongst themselves"

        then: "The arithmetic operations work as expected"

        verifyAll {
            b.subtract(a).equals(gap.rescaledTo(24))
            a.add(gap).equals(b.rescaledTo(48))
            b.subtract(gap).equals(a)
        }
    }

    def "Convert non drop frame timecode to RationalTime at drop frame rate"() {
        given: "A non drop frame timecode and the corresponding number of frames at 29.97 fps"
        def DF_TC = "01:00:02;05"
        def NDF_TC = "00:59:58:17"
        def frames = 107957

        def tc1 = new RationalTime(frames, 29.97).toTimecode()
        verifyAll {
            tc1 == DF_TC
        }
        def tc2 = new RationalTime(frames, 29.97).toTimecode(29.97, IsDropFrameRate.ForceNo)
        verifyAll {
            tc2 == NDF_TC
        }

        when: "The NDF timecode is converted to RationalTime at DF rate"
        def t = RationalTime.fromTimecode(NDF_TC, 29.97)
        def t2 = RationalTime.fromTimecode(DF_TC, 29.97)

        then: "The conversion is still handled correctly"

        verifyAll {
            t.getValue() == frames
            t2.getValue() == frames
        }
    }

}