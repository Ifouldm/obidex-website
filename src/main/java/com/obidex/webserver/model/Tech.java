package com.obidex.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * A Enum for the available technologies that can be associated with projects etc.
 */
@Getter
@AllArgsConstructor
public enum Tech {
    JAVA("Java", "fab fa-java", "https://www.java.com/"),
    SPRING("Spring", "icon icon-spring", "https://spring.io/"),
    FLUTTER("Flutter", "icon icon-flutter", "https://flutter.dev/"),
    DART("Dart", "icon icon-dart", "https://dart.dev/"),
    BOOTSTRAP("Bootstrap", "fab fa-bootstrap", "https://getbootstrap.com/"),
    MONGO_DB("Mongo DB", "icon icon-mongo", "https://www.mongodb.com/"),
    THYMELEAF("Thymeleaf", "icon icon-thymeleaf", "https://www.thymeleaf.org/"),
    HTML("HTML 5", "fab fa-html5", "https://www.w3.org/"),
    JAVASCRIPT("JavaScript", "fab fa-js-square", "#"),
    CSS("CSS", "fab fa-css3", "#"),
    GOOGLE_CLOUD("Google Cloud", "icon icon-googleCloud", "https://cloud.google.com/"),
    GRADLE("Gradle", "icon icon-gradle", "https://gradle.org/"),
    PYTHON("Python", "fab fa-python", "https://www.python.org/"),
    VUE("Vue", "fab fa-vuejs", "https://vuejs.org/"),
    NODE("Node JS", "fab fa-node", "https://nodejs.org/"),
    NPM("NPM", "fab fa-npm", "https://www.npmjs.com/"),
    REACT("React", "fab fa-react", "https://reactjs.org/");

    private final String name;
    private String icon;
    private String link;

    Tech(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    Tech(String name) {
        this.name = name;
    }

    boolean hasLink() {
        return link != null;
    }

    boolean hasIcon() {
        return icon != null;
    }
}
