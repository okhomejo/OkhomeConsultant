package id.co.okhome.consultant.model;


import java.util.Map;

public class MoneyHistoryModel {
//      `id` bigint(20) NOT NULL AUTO_INCREMENT,
//  `consultant_id` int(11) DEFAULT NULL,
//  `use_type` char(1) DEFAULT NULL COMMENT 'U(se)/G(et)',
//            `tag` char(1) DEFAULT NULL COMMENT 'C(leaning)\nW(ithdrawal)\n',
//            `money` int(11) DEFAULT NULL,
//  `extra` varchar(1000) DEFAULT NULL,
//  `insert_datetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,

    public int rownum;

    public long id;
    public int money;
    public int consultantId;
    public String useType; //U(se)/G(et)
    public String insertDateTime;
    public String finishYN; //예정이면 N 완료된거면 Y
    public String payDateTime;
    public String tag;
    public Map<String, Object> cleaning;
    public Map<String, Object> penalty;

}
