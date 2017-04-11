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
        given:
        domain.clearErrors()

        when:
        domain.name = null

        then:
        !domain.validate(['name'])
        domain.errors['name'].code == 'nullable'
    }

    void "test name cannot be blank"() {
        given:
        domain.clearErrors()

        when:
        domain.name = ''

        then:
        !domain.validate(['name'])
    }

    void "test name can have a maximum of 255 characters"() {
        given:
        domain.clearErrors()

        when: 'for a string of 256 characters'
        String str = 'a'*256
        domain.name = str

        then: 'name validation fails'
        !domain.validate(['name'])
        domain.errors['name'].code == 'maxSize.exceeded'

        when: 'for a string of 256 characters'
        str = 'a'*255
        domain.name = str

        then: 'name validation passes'
        domain.validate(['name'])
    }
    // end::nameTests[]

    // tag::urlTests[]
    void "test url can have a maximum of 255 characters"() {
        given:
        domain.clearErrors()

        when: 'for a string of 256 characters'
        String urlprefifx = 'http://'
        String urlsufifx = '.com'
        String str = 'a'*(256 - (urlprefifx.size() + urlsufifx.size())) 
        str = urlprefifx + str + urlsufifx
        domain.url = str

        then: 'url validation fails'
        !domain.validate(['url'])
        domain.errors['url'].code == 'maxSize.exceeded'

        when: 'for a string of 256 characters'
        str = "${urlprefifx}${'a'*(255 - (urlprefifx.size() + urlsufifx.size()))}${urlsufifx}"
        domain.url = str
        
        then: 'url validation passes'
        domain.validate(['url'])
    }

    @Unroll('Hotel.validate() with url: #value should have returned #expected with errorCode: #expectedErrorCode')
    void "test url validation"() {
        given:
        domain.clearErrors()

        when:
        domain.url = value

        then:
        expected == domain.validate(['url'])
        domain.errors['url']?.code == expectedErrorCode

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
        given:
        domain.clearErrors()

        when:
        domain.email = value

        then:
        expected == domain.validate(['email'])
        domain.errors['email']?.code == expectedErrorCode

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
        given:
        domain.clearErrors()

        when:
        domain.about = null

        then:
        domain.validate(['about'])
    }

    void "test about can be blank"() {
        when:
        domain.about = ''

        then:
        domain.validate(['about'])
    }

    void "test about can have a more than 255 characters"() {
        given:
        domain.clearErrors()

        when: 'for a string of 256 characters'
        String str = 'a'*256
        domain.about = str

        then: 'about validation passes'
        domain.validate(['about'])
    }
    // end::aboutTests[]

    // tag::latitudeAndLongitudeTests[]
    @Unroll('Hotel.validate() with latitude: #value should have returned #expected with errorCode: #expectedErrorCode')
    void "test latitude validation"() {
        given:
        domain.clearErrors()

        when:
        domain.latitude = value

        then:
        expected == domain.validate(['latitude'])
        domain.errors['latitude']?.code == expectedErrorCode

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
        given:
        domain.clearErrors()

        when:
        domain.longitude = value

        then:
        expected == domain.validate(['longitude'])
        domain.errors['longitude']?.code == expectedErrorCode

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
