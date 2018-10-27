package com.expo.network.response;

import java.util.List;

public class VersionInfoResp extends BaseResponse{


    /**
     * Objlst : [{"apkfilesize":0,"apkurl":"string","id":0,"isforce":"string","platformname":"string","remark":"string","remarken":"string","resfilesize":0,"resurl":"string","resver":"string","updatetime":"string","ver":"string"}]
     * UpdateTime : string
     */

    public String UpdateTime;
    public List<ObjlstBean> Objlst;

    public static class ObjlstBean {
        /**
         * apkfilesize : 0
         * apkurl : string
         * id : 0
         * isforce : string
         * platformname : string
         * remark : string
         * remarken : string
         * resfilesize : 0
         * resurl : string
         * resver : string
         * updatetime : string
         * ver : string
         */

        public int apkfilesize;
        public String apkurl;
        public int id;
        public String isforce;
        public String platformname;
        public String remark;
        public String remarken;
        public int resfilesize;
        public String resurl;
        public String resver;
        public String updatetime;
        public String ver;
    }
}
