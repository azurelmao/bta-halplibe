package turniplabs.halplibe.helper;

import turniplabs.halplibe.util.achievements.AchievementPage;

import java.util.ArrayList;
import java.util.List;

public final class AchievementHelper {
    private static final List<AchievementPage> pages = new ArrayList<>();

    public static List<AchievementPage> getPages() {
        return pages;
    }

    public static AchievementPage getPage(int index){
        if(index+1 > pages.size() || index < 0){
            return null;
        } else {
            return pages.get(index);
        }
    }

    public static void addPage(AchievementPage page){
        pages.add(page);
    }

    public static String[] getPageNames(){
        List<String> list = new ArrayList<>();
        for (AchievementPage page : pages) {
            list.add(page.name);
        }
        return list.toArray(new String[0]);
    }

    public static String[] getPageDescriptions(){
        List<String> list = new ArrayList<>();
        for (AchievementPage page : pages) {
            list.add(page.getDescription());
        }
        return list.toArray(new String[0]);
    }
}
