package lab.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sites {
    private List<Site> siteList = new ArrayList<>();

    public void add(Site site) {
        this.siteList.add(site);
    }

    public void clear() {
        this.siteList.clear();
    }

    public Site get(int index) {
        try {
            return siteList.get(index);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public boolean isEmpty() {
        return siteList.isEmpty();
    }

    public int size() {
        return siteList.size();
    }

    public void sortByTitle() {
        siteList.sort(new Comparator<Site>() {
            @Override
            public int compare(Site site1, Site site2) {
                return site1.getTitle().compareTo(site2.getTitle());
            }
        });
    }

    public void sortByType() {
        siteList.sort(new Comparator<Site>() {
            @Override
            public int compare(Site site1, Site site2) {
                return site1.getType().compareTo(site2.getType());
            }
        });
    }
}
