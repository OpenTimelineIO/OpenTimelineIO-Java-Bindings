package io.opentimeline

import io.opentimeline.opentime.RationalTime
import io.opentimeline.opentime.TimeRange
import io.opentimeline.opentimelineio.Item
import spock.lang.Specification

class ItemSpockTest extends Specification {

    def "Test item is disabled"() {
        given: "An item is created"
        def item = new Item.ItemBuilder()
                .setSourceRange(new TimeRange.TimeRangeBuilder()
                        .setDuration(new RationalTime(10, 1))
                        .build())
                .build()

        verifyAll {
            item.isEnabled()
        }

        when: "Item is disabled"
        item.setEnabled(false)

        then: "Item does not contribute to the composition"
        verifyAll {
            (!item.isEnabled())
        }
    }
}