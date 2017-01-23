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
        expect:
        !new Hotel(name: null).validate(['name'])
    }

    void "test name cannot be blank"() {
        expect:
        !new Hotel(name: '').validate(['name'])
    }

    void "test name can have a maximum of 255 characters"() {
        when: 'for a string of 256 characters'
        String str = ''
        256.times { str += 'a' }

        then: 'name validation fails'
        !new Hotel(name: str).validate(['name'])

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

        then: 'url validation fails'
        !new Hotel(url: str).validate(['url'])

        when: 'for a string of 256 characters'
        str = ''
        (255 - (urlprefifx.size() + urlsufifx.size())).times { str += 'a' }
        str = urlprefifx + str + urlsufifx

        then: 'url validation passes'
        new Hotel(url: str).validate(['url'])
    }

    @Unroll('Hotel.validate() with url: #value should have returned #expected')
    void "test url validation"() {
        when:
        def result = new Hotel(url: value).validate(['url'])

        then:
        expected == result

        where:
        value                  || expected
        null                   |  true
        ''                     |  true
        'http://hilton.com'    |  true
        'hilton'               |  false
    }
    // end::urlTests[]

    @Unroll('Hotel.validate() with email: #value should have returned #expected')
    // tag::emailTests[]
    void "test email validation"() {
        when:
        def result = new Hotel(email: value).validate(['email'])

        then:
        expected == result

        where:
        value                  || expected
        null                   |  true
        ''                     |  true
        'contact@hilton.com'   |  true
        'hilton'               |  false
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

    @Unroll('Hotel.validate() with latitude: #value should have returned #expected')
    // tag::latitudeAndLongitudeTests[]
    void "test latitude validation"() {
        when:
        def result = new Hotel(latitude: value).validate(['latitude'])

        then:
        expected == result

        where:
        value                  || expected
        null                   |  true
        0                      |  true
        0.5                    |  true
        90                     |  true
        90.5                   |  false
        -90                    |  true
        -180                   |  false
        180                    |  false
    }

    @Unroll('Hotel.longitude() with latitude: #value should have returned #expected')
    void "test longitude validation"() {
        when:
        def result = new Hotel(longitude: value).validate(['longitude'])

        then:
        expected == result

        where:
        value                  || expected
        null                   |  true
        0                      |  true
        90                     |  true
        90.1                   |  true
        -90                    |  true
        -180                   |  true
        180                    |  true
        180.1                  |  false
        -180.1                 |  false
    }
    // end::latitudeAndLongitudeTests[]
}
