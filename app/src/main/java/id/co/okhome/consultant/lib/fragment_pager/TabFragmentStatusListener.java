package id.co.okhome.consultant.lib.fragment_pager;

import java.util.Map;

/**
 * Created by josong on 2017-08-18.
 */

public interface TabFragmentStatusListener {
    void onSelect();
    void onDeselect();
    void onSelectWithData(Map<String, Object> param);
}
