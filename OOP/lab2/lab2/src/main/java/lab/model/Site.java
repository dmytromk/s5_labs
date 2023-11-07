package lab.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return hasAuthorization == site.hasAuthorization &&
                Objects.equals(id, site.id) &&
                Objects.equals(title, site.title) &&
                type == site.type &&
                Objects.equals(characteristics, site.characteristics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, type, hasAuthorization, characteristics);
    }
}

