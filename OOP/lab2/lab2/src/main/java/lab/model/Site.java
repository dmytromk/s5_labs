package lab.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Site {

    public enum SiteType {
        Advertisement,
        Mirror,
        News,
        Portal,
    }

    private String id;
    private String title;
    private SiteType type;
    private boolean hasAuthorization;
    private Characteristics characteristics;

    public Site() {
        Characteristics characteristics = new Characteristics();
    }

    public Site(Characteristics characteristics) {
        this.characteristics = characteristics;
    }

    public Site(String id, String title, SiteType type, boolean hasAuthorization) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.hasAuthorization = hasAuthorization;
    }

    public Site(String id, String title, SiteType type, boolean hasAuthorization, Characteristics characteristics) {
        this(id, title, type, hasAuthorization);
        this.characteristics = characteristics;
    }
}

