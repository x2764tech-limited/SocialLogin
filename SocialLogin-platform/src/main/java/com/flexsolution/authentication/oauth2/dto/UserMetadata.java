package com.flexsolution.authentication.oauth2.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class UserMetadata {

    private String id;
    /**
     * The member's first name.
     */
    private String firstName;
    /**
     * The member's last name.
     */
    private String lastName;
    /**
     * The LinkedIn member's primary email address.
     * Secondary email addresses associated with the member are not available via the API.
     */
    private String emailAddress;
    /**
     * A URL to the member's formatted profile picture, if one has been provided.
     */
    private String pictureUrl;
    /**
     * The industry the member belongs to.
     */
    private String industry;
    /**
     * The member's headline.
     */
    private String headline;
    /**
     * The URL to the member's public profile on LinkedIn.
     */
    private String publicProfileUrl;
    /**
     * A long-form text area describing the member's professional profile.
     */
    private String summary;
    /**
     * An object representing the user's physical location.
     */
    private Location location = new Location();

    @Override
    public String toString() {
        return "UserMetadata{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Setter
    @Getter
    @ToString
    public class Location {
        /**
         * A generic name representing the location of the member.
         * This is a pseudo-curated list generated from user input, and as such,
         * there is no API call or static reference page that will list all possible for this field.
         * e.g. "San Francisco Bay Area"
         */
        private String name;
    }
}
