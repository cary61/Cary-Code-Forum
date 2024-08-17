package top.cary.cary_code.utils.matcher;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.stereotype.Component;

@Component
public class Matcher {

    public int fuzzyMatch(String str1, String str2) {
        int ratio =  FuzzySearch.ratio(str1, str2);
//        System.out.println(ratio);
        return ratio;
    }
}
