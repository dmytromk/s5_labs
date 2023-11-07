package lab.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Characteristic {

    public Characteristic() {
    }

    public enum VotingType {
        No,
        Anonymous,
        Authorized
    }

    private boolean hasEmail;
    private boolean hasNews;
    private boolean hasArchive;
    private boolean isFree;
    private VotingType votingType;

    public Characteristic(boolean hasEmail, boolean hasNews, boolean hasArchive, boolean isFree, VotingType votingType) {
        this.hasEmail = hasEmail;
        this.hasNews = hasNews;
        this.hasArchive = hasArchive;
        this.isFree = isFree;
        this.votingType = votingType;
    }

    @Override
    public String toString() {
        return "\n\tSite has email? " + hasEmail + "\n\tSite has news? " + hasNews
                + "\n\tSite has archive? " + hasArchive + "\n\tSite is free? " + isFree
                + "\n\tSite's type of voting? " + votingType;
    }
}
