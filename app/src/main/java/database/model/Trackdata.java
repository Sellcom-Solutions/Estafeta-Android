package database.model;

import android.content.ContentValues;
import android.content.Context;

import java.util.Map;

import database.DataBaseAdapter;

/**
 * Created by rebecalopezmartinez on 28/05/15.
 * This class contains methods that let you edit the table 'Trackdata' Database.
 */
public class Trackdata {
    public static final String TABLE_NAME = "trackdata";

    public static final String ID_TRACKDATA = "id_trackdata";
    public static final String WAYBILL = "waybill";
    public static final String SHORTWAYBILL = "shortwaybill";
    public static final String SRVCID = "srvcid";
    public static final String SRVCDESCSPA = "srvcdescspa";
    public static final String SRVCDESCENG = "srvcdesceng";
    public static final String CUSTNUMBER = "custnumber";
    public static final String PACKAGETYPE = "packagetype";
    public static final String ADTNLINFO = "adtnlinfo";
    public static final String STATUSSPA = "statusspa";
    public static final String STATUSENG = "statuseng";
    public static final String PD_ORG_ACRONYM = "pd_org_acronym";
    public static final String PD_ORG_NAME = "pd_org_name";
    public static final String PD_PKUP_DATE = "pd_pkup_date";
    public static final String DD_DST_ACRONYM = "dd_dst_acronym";
    public static final String DD_DST_NAME = "dd_dst_name";
    public static final String DD_DLVRY_DATE = "dd_dlvry_date";
    public static final String DD_ZIP = "dd_zip";
    public static final String DD_RCVR = "dd_rcvr";
    public static final String DM_WEIGHT = "dm_weight";
    public static final String DM_VOLWEIGHT = "dm_volweight";
    public static final String DM_WIDTH = "dm_with";
    public static final String DM_LENGTH = "dm_length";
    public static final String DM_HEIGHT = "dm_height";
    public static final String WBRD_ORG_WAYBILL = "wbrd_org_waybill";
    public static final String WBRD_RPLC_WAYBILL = "wbrd_rplc_waybill";
    public static final String RTRNDD_INITL_WAYBILL = "rtrndd_initl_waybill";
    public static final String RTRNDD_FINAL_WAYBILL = "rtrndd_final_waybill";
    public static final String MSD_PREC_WAYBILLS = "msd_prec_waybills";
    public static final String MSD_FOLL_WAYBILLS = "msd_foll_waybills";
    public static final String MSD_WB_LIST = "msd_wb_list";
    public static final String INITID_INTL_WAYBILL = "initid_initl_waybill";
    public static final String INITID_ORG_COUNTRYCODE = "initId_org_countrycode";
    public static final String INITID_ORG_COUNTRYSPA = "initId_org_countryspa";
    public static final String INITID_ORG_COUNTRYENG = "initId_org_countryeng";
    public static final String CSTMINFO_REFERENCE = "cstminfo_reference";
    public static final String CSTMINFO_COSTSCENTRE = "cstminfo_costsCentre";
    public static final String ID_CTL_FAVORITOS = "id_ctl_favoritos";

    private int id_trackdata;
    private String waybill;
    private String shortwaybill;
    private String srvcid;
    private String srvcdescspa;
    private String srvcdesceng;
    private String custnumber;
    private String packagetype;
    private String adtnlinfo;
    private String statusspa;
    private String statuseng;
    private String pd_org_acronym;
    private String pd_org_name;
    private String pd_pkup_date;
    private String dd_dst_acronym;
    private String dd_dst_name;
    private String dd_dlvry_date;
    private String dd_zip;
    private String dd_rcvr;
    private String dm_weight;
    private String dm_volweight;
    private String dm_width;
    private String dm_length;
    private String dm_height;
    private String wbrd_org_waybill;
    private String wbrd_rplc_waybill;
    private String rtrndd_initl_waybill;
    private String rtrndd_final_waybill;
    private String msd_prec_waybills;
    private String msd_foll_waybills;
    private String msd_wb_list;
    private String initId_initl_waybill;
    private String initId_org_countrycode;
    private String iniId_org_countryspa;
    private String initId_org_countryeng;
    private String cstminfo_reference;
    private String cstminfo_costsCentre;
    private int id_ctl_favoritos;

    public Trackdata(int id_trackdata, String waybill, String shortwaybill, String srvcid, String srvcdescspa,
                     String srvcdesceng, String custnumber, String packagetype, String adtnlinfo, String statusspa,
                     String statuseng, String pd_org_acronym, String pd_org_name, String pd_pkup_date, String dd_dst_acronym,
                     String dd_dst_name, String dd_dlvry_date, String dd_zip, String dd_rcvr, String dm_weight, String dm_volweight,
                     String dm_width, String dm_length, String dm_height, String wbrd_org_waybill, String wbrd_rplc_waybill,
                     String rtrndd_initl_waybill,String rtrndd_final_waybill, String msd_prec_waybills, String msd_foll_waybills,
                     String msd_wb_list, String initId_org_countrycode, String initId_initl_waybill, String iniId_org_countryspa,
                     String initId_org_countryeng, String cstminfo_reference, String cstminfo_costsCentre, int id_ctl_favoritos) {

        this.id_trackdata = id_trackdata;
        this.waybill = waybill;
        this.shortwaybill = shortwaybill;
        this.srvcid = srvcid;
        this.srvcdescspa = srvcdescspa;
        this.srvcdesceng = srvcdesceng;
        this.custnumber = custnumber;
        this.packagetype = packagetype;
        this.adtnlinfo = adtnlinfo;
        this.statusspa = statusspa;
        this.statuseng = statuseng;
        this.pd_org_acronym = pd_org_acronym;
        this.pd_org_name = pd_org_name;
        this.pd_pkup_date = pd_pkup_date;
        this.dd_dst_acronym = dd_dst_acronym;
        this.dd_dst_name = dd_dst_name;
        this.dd_dlvry_date = dd_dlvry_date;
        this.dd_zip = dd_zip;
        this.dd_rcvr = dd_rcvr;
        this.dm_weight = dm_weight;
        this.dm_volweight = dm_volweight;
        this.dm_width = dm_width;
        this.dm_length = dm_length;
        this.dm_height = dm_height;
        this.wbrd_org_waybill = wbrd_org_waybill;
        this.wbrd_rplc_waybill = wbrd_rplc_waybill;
        this.rtrndd_initl_waybill = rtrndd_initl_waybill;
        this.rtrndd_final_waybill = rtrndd_final_waybill;
        this.msd_prec_waybills = msd_prec_waybills;
        this.msd_foll_waybills = msd_foll_waybills;
        this.msd_wb_list = msd_wb_list;
        this.initId_org_countrycode = initId_org_countrycode;
        this.initId_initl_waybill = initId_initl_waybill;
        this.iniId_org_countryspa = iniId_org_countryspa;
        this.initId_org_countryeng = initId_org_countryeng;
        this.cstminfo_reference = cstminfo_reference;
        this.cstminfo_costsCentre = cstminfo_costsCentre;
        this.id_ctl_favoritos = id_ctl_favoritos;
    }

    /**
     * It allows you to insert data into the database.
     * @param context
     * @param waybill
     * @param shortwaybill
     * @param srvcid
     * @param srvcdescspa
     * @param srvcdesceng
     * @param custnumber
     * @param packagetype
     * @param adtnlinfo
     * @param statusspa
     * @param statuseng
     * @param pd_org_acronym
     * @param pd_org_name
     * @param pd_pkup_date
     * @param dd_dst_acronym
     * @param dd_dst_name
     * @param dd_dlvry_date
     * @param dd_zip
     * @param dd_rcvr
     * @param dm_weight
     * @param dm_volweight
     * @param dm_width
     * @param dm_length
     * @param dm_height
     * @param wbrd_org_waybill
     * @param wbrd_rplc_waybill
     * @param rtrndd_initl_waybill
     * @param rtrndd_final_waybill
     * @param msd_prec_waybills
     * @param msd_foll_waybills
     * @param msd_wb_list
     * @param initId_org_countrycode
     * @param initId_initl_waybill
     * @param iniId_org_countryspa
     * @param initId_org_countryeng
     * @param cstminfo_reference
     * @param cstminfo_costsCentre
     * @param id_ctl_favoritos
     * @return
     */
    public static long insert(Context context, String waybill, String shortwaybill, String srvcid, String srvcdescspa,
                              String srvcdesceng, String custnumber, String packagetype, String adtnlinfo, String statusspa,
                              String statuseng, String pd_org_acronym, String pd_org_name, String pd_pkup_date, String dd_dst_acronym,
                              String dd_dst_name, String dd_dlvry_date, String dd_zip, String dd_rcvr, String dm_weight, String dm_volweight,
                              String dm_width, String dm_length, String dm_height, String wbrd_org_waybill, String wbrd_rplc_waybill,
                              String rtrndd_initl_waybill,String rtrndd_final_waybill, String msd_prec_waybills, String msd_foll_waybills,
                              String msd_wb_list, String initId_org_countrycode, String initId_initl_waybill, String iniId_org_countryspa,
                              String initId_org_countryeng, String cstminfo_reference, String cstminfo_costsCentre, int id_ctl_favoritos){

        ContentValues cv = new ContentValues();
        cv.put(WAYBILL, waybill);
        cv.put(SHORTWAYBILL, shortwaybill);
        cv.put(SRVCID, srvcid);
        cv.put(SRVCDESCSPA, srvcdescspa);
        cv.put(SRVCDESCENG, srvcdesceng);
        cv.put(CUSTNUMBER, custnumber);
        cv.put(PACKAGETYPE, packagetype);
        cv.put(ADTNLINFO, adtnlinfo);
        cv.put(STATUSSPA, statusspa);
        cv.put(STATUSENG, statuseng);
        cv.put(PD_ORG_ACRONYM, pd_org_acronym);
        cv.put(PD_ORG_NAME, pd_org_name);
        cv.put(PD_PKUP_DATE, pd_pkup_date);
        cv.put(DD_DST_ACRONYM, dd_dst_acronym);
        cv.put(DD_DST_NAME, dd_dst_name);
        cv.put(DD_DLVRY_DATE, dd_dlvry_date);
        cv.put(DD_ZIP, dd_zip);
        cv.put(DD_RCVR, dd_rcvr);
        cv.put(DM_WEIGHT, dm_weight);
        cv.put(DM_VOLWEIGHT, dm_volweight);
        cv.put(DM_WIDTH, dm_width);
        cv.put(DM_LENGTH, dm_length);
        cv.put(DM_HEIGHT, dm_height);
        cv.put(WBRD_ORG_WAYBILL, wbrd_org_waybill);
        cv.put(WBRD_RPLC_WAYBILL, wbrd_rplc_waybill);
        cv.put(RTRNDD_INITL_WAYBILL, rtrndd_initl_waybill);
        cv.put(RTRNDD_FINAL_WAYBILL, rtrndd_final_waybill);
        cv.put(MSD_PREC_WAYBILLS, msd_prec_waybills);
        cv.put(MSD_FOLL_WAYBILLS, msd_foll_waybills);
        cv.put(MSD_WB_LIST, msd_wb_list);
        cv.put(INITID_INTL_WAYBILL, initId_initl_waybill);
        cv.put(INITID_ORG_COUNTRYCODE, initId_org_countrycode);
        cv.put(INITID_ORG_COUNTRYSPA, iniId_org_countryspa);
        cv.put(INITID_ORG_COUNTRYENG, initId_org_countryeng);
        cv.put(CSTMINFO_REFERENCE, cstminfo_reference);
        cv.put(CSTMINFO_COSTSCENTRE, cstminfo_costsCentre);
        cv.put(ID_CTL_FAVORITOS, id_ctl_favoritos);

        return DataBaseAdapter.getDB(context).insert(TABLE_NAME,null,cv);

    }

    /**
     * It allows you to insert data into the database.
     * @param values
     * @return
     */
    public static long insert(Map<String, String> values){
        ContentValues cv = new ContentValues();

        cv.put(WAYBILL, values.get("wayBill"));
        cv.put(SHORTWAYBILL, values.get("shortWayBillId"));
        cv.put(SRVCID, values.get("serviceId"));
        cv.put(SRVCDESCSPA, values.get("serviceDescriptionSPA"));
        cv.put(SRVCDESCENG, values.get("serviceDescriptionENG"));
        cv.put(CUSTNUMBER, values.get("customerNumber"));
        cv.put(PACKAGETYPE, values.get("packageType"));
        cv.put(ADTNLINFO, values.get("additionalInformation"));
        cv.put(STATUSSPA, values.get("statusSPA"));
        cv.put(STATUSENG, values.get("statusENG"));
        cv.put(PD_ORG_ACRONYM, values.get("PK_originAcronym"));
        cv.put(PD_ORG_NAME, values.get("PK_originName"));
        cv.put(PD_PKUP_DATE, values.get("PK_pickupDateTime"));
        cv.put(DD_DST_ACRONYM, values.get("DD_destinationAcronym"));
        cv.put(DD_DST_NAME, values.get("DD_destinationName"));
        cv.put(DD_DLVRY_DATE, values.get("DD_deliveryDateTime"));
        cv.put(DD_ZIP, values.get("DD_zipCode"));
        cv.put(DD_RCVR, values.get("DD_receiverName"));
        cv.put(DM_WEIGHT, values.get("Dim_weight"));
        cv.put(DM_VOLWEIGHT, values.get("Dim_volumetricWeight"));
        cv.put(DM_WIDTH, values.get("Dim_width"));
        cv.put(DM_LENGTH, values.get("Dim_length"));
        cv.put(DM_HEIGHT, values.get("Dim_height"));

        cv.put(WBRD_ORG_WAYBILL, values.get(""));
        cv.put(WBRD_RPLC_WAYBILL, values.get(""));
        cv.put(RTRNDD_INITL_WAYBILL, values.get(""));
        cv.put(RTRNDD_FINAL_WAYBILL, values.get(""));
        cv.put(MSD_PREC_WAYBILLS, values.get(""));
        cv.put(MSD_FOLL_WAYBILLS, values.get(""));
        cv.put(MSD_WB_LIST, values.get(""));
        cv.put(INITID_INTL_WAYBILL, values.get(""));
        cv.put(INITID_ORG_COUNTRYCODE, values.get(""));
        cv.put(INITID_ORG_COUNTRYSPA, values.get(""));
        cv.put(INITID_ORG_COUNTRYENG, values.get(""));

        cv.put(CSTMINFO_REFERENCE, values.get("CI_reference"));
        cv.put(CSTMINFO_COSTSCENTRE, values.get("CI_costsCentre"));
        cv.put(ID_CTL_FAVORITOS, values.get("id_ctl_favoritos"));

        return 0;
    }
}
