package demo

import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
// tag::testForTests[]
@TestFor(Hotel)
class HotelSpec extends Specification {
// end::testForTests[]

    // tag::nameTests[]
    void "test name cannot be null"() {
        when:
        def hotel = new Hotel(name: null)

        then:
        !hotel.validate(['name'])
        hotel.errors['name'].code == 'nullable'
    }

    void "test name cannot be blank"() {
        expect:
        !new Hotel(name: '').validate(['name'])
    }

    void "test name can have a maximum of 255 characters"() {
        when: 'for a string of 256 characters'
        String str = ''
        256.times { str += 'a' }
        def hotel = new Hotel(name: str)

        then: 'name validation fails'
        !hotel.validate(['name'])
        hotel.errors['name'].code == 'maxSize.exceeded'

        when: 'for a string of 256 characters'
        str = ''
        255.times { str += 'a' }

        then: 'name validation passes'
        new Hotel(name: str).validate(['name'])
    }
    // end::nameTests[]

    // tag::urlTests[]
    void "test url can have a maximum of 255 characters"() {
        when: 'for a string of 256 characters'
        String urlprefifx = 'http://'
        String urlsufifx = '.com'
        String str = ''
        (256 - (urlprefifx.size() + urlsufifx.size())).times { str += 'a' }
        str = urlprefifx + str + urlsufifx
        def hotel = new Hotel(url: str)

        then: 'url validation fails'
        !hotel.validate(['url'])
        hotel.errors['url'].code == 'maxSize.exceeded'

        when: 'for a string of 256 characters'
        str = ''
        (255 - (urlprefifx.size() + urlsufifx.size())).times { str += 'a' }
        str = urlprefifx + str + urlsufifx

        then: 'url validation passes'
        new Hotel(url: str).validate(['url'])
    }

    @Unroll('Hotel.validate() with url: #value should have returned #expected with errorCode: #expectedErrorCode')
    void "test url validation"() {
        when:
        def hotel = new Hotel(url: value)

        then:
        expected == hotel.validate(['url'])
        hotel.errors['url']?.code == expectedErrorCode

        where:
        value                  | expected | expectedErrorCode
        null                   | true     | null
        ''                     | true     | null
        'http://hilton.com'    | true     | null
        'hilton'               | false    | 'url.invalid'
    }
    // end::urlTests[]

    // tag::emailTests[]
    @Unroll('Hotel.validate() with email: #value should have returned #expected with errorCode: #expectedErrorCode')
    void "test email validation"() {
        when:
        def hotel = new Hotel(email: value)

        then:
        expected == hotel.validate(['email'])
        hotel.errors['email']?.code == expectedErrorCode

        where:
        value                  | expected | expectedErrorCode
        null                   |  true    | null
        ''                     |  true    | null
        'contact@hilton.com'   |  true    | null
        'hilton'               |  false   | 'email.invalid'
    }
    // end::emailTests[]

    // tag::aboutTests[]
    void "test about can be null"() {
        expect:
        new Hotel(about: null).validate(['about'])
    }

    void "test about can be blank"() {
        expect:
        new Hotel(about: '').validate(['about'])
    }

    void "test about can have a more than 255 characters"() {

        when: 'for a string of 256 characters'
        String str = ''
        256.times { str += 'a' }

        then: 'about validation passes'
        new Hotel(about: str).validate(['about'])
    }
    // end::aboutTests[]

    // tag::latitudeAndLongitudeTests[]
    @Unroll('Hotel.validate() with latitude: #value should have returned #expected with errorCode: #expectedErrorCode')
    void "test latitude validation"() {
        when:
        def hotel = new Hotel(latitude: value)

        then:
        expected == hotel.validate(['latitude'])
        hotel.errors['latitude']?.code == expectedErrorCode

        where:
        value                  | expected | expectedErrorCode
        null                   | true     | null
        0                      | true     | null
        0.5                    | true     | null
        90                     | true     | null
        90.5                   | false    | 'range.toobig'
        -90                    | true     | null
        -180                   | false    | 'range.toosmall'
        180                    | false    | 'range.toobig'
    }

    @Unroll('Hotel.longitude() with latitude: #value should have returned #expected with error code: #expectedErrorCode')
    void "test longitude validation"() {
        when:
        def hotel = new Hotel(longitude: value)

        then:
        expected == hotel.validate(['longitude'])
        hotel.errors['longitude']?.code == expectedErrorCode

        where:
        value                  | expected | expectedErrorCode
        null                   | true     | null
        0                      | true     | null
        90                     | true     | null
        90.1                   | true     | null
        -90                    | true     | null
        -180                   | true     | null
        180                    | true     | null
        180.1                  | false    | 'range.toobig'
        -180.1                 | false    | 'range.toosmall'
    }
    // end::latitudeAndLongitudeTests[]
}
