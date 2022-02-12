package io.opentimeline

import io.opentimeline.opentimelineio.Algorithms
import io.opentimeline.opentimelineio.SerializableObject
import io.opentimeline.opentimelineio.Stack
import io.opentimeline.opentimelineio.Track
import spock.lang.Specification
import util.TestUtil

class StackAlgoBDDTest extends Specification{

    def trackZd
    def track_d
    def trackABC

    def setup() {
        trackZd = (Track) SerializableObject.fromJSONString(this.getClass().getClassLoader().getResourceAsStream('trackZd.otio').text)
        track_d = (Track) SerializableObject.fromJSONString(this.getClass().getClassLoader().getResourceAsStream('track_d.otio').text)
        trackABC = (Track) SerializableObject.fromJSONString(this.getClass().getClassLoader().getResourceAsStream('trackABC.otio').text)
    }

    def cleanup() {
        try {
            trackZd.close()
            track_d.close()
            trackABC.close()
        } catch(Exception e) {
            e.printStackTrace()
        }
    }

    def "Flatten disabled clip"() {
        given: "A stack with two tracks, one with a 150 frames long disabled clip over one with 3 enabled clips (A, B, C) 50 frames long"
        def stack = new Stack.StackBuilder().build()
        verifyAll {
            stack.appendChild(trackABC)
            stack.appendChild(track_d)
        }

        when: "The stack is flattened"
        def flatTrack = new Algorithms().flattenStack(stack)
        flatTrack.setName(trackABC.getName())

        then: "The disabled clip does not contribute to the composition, and the flattened track has three clips (A, B, C)"
        verifyAll {
            TestUtil.isJSONEqual(flatTrack.toJSONString(), trackABC.toJSONString())
        }

        try {
            stack.close()
            flatTrack.close()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    def "Flatten disabled track"() {
        given: "A stack with two tracks, one disabled track with a 150 frames long clip over one with 3 enabled clips (A, B, C) 50 frames long"
        def stack = new Stack.StackBuilder().build()
        verifyAll {
            stack.appendChild(trackABC)
            stack.appendChild(track_d)
        }

        when: "The stack is flattened"
        def flatTrack = new Algorithms().flattenStack(stack)
        flatTrack.setName(trackABC.getName())

        then: "The disabled track does not contribute to the composition, and the flattened track has three clips (A, B, C)"
        verifyAll {
            TestUtil.isJSONEqual(flatTrack.toJSONString(), trackABC.toJSONString())
        }

        try {
            stack.close()
            flatTrack.close()
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

}
