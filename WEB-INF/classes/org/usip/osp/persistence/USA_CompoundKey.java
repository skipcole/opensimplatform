package org.usip.osp.persistence;

import javax.persistence.Embeddable;


/**
 * @author Ronald "Skip" Cole
 * 
 * This file is part of the USIP Open Simulation Platform.<br>
 * 
 * The USIP Open Simulation Platform is free software; you can
 * redistribute it and/or modify it under the terms of the new BSD Style license
 * associated with this distribution.<br>
 * 
 * The USIP Open Simulation Platform is distributed WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. <BR>
 * 
 */
@Embeddable
public class USA_CompoundKey implements java.io.Serializable{

    private Long bu_id;
    private Long schema_id;
    public USA_CompoundKey(){}
    public USA_CompoundKey(Long bu, Long si){
        bu_id = bu;
        schema_id = si;
    }
    public Long getBu_id() {
        return bu_id;
    }
    public void setBu_id(Long bu_id) {
        this.bu_id = bu_id;
    }
    public Long getSchema_id() {
        return schema_id;
    }
    public void setSchema_id(Long schema_id) {
        this.schema_id = schema_id;
    }
    
    public boolean equals (Object key){
        if (key == null) {
            return false;
        }
        if (!(key instanceof USA_CompoundKey)){
            return false;
        }
        USA_CompoundKey usa_ck = (USA_CompoundKey) key;
        
        if (!(usa_ck.getBu_id().equals(bu_id))){
            return false;
        }
        
        if (!(usa_ck.getSchema_id().equals(schema_id))){
            return false;
        }
        
        return true;
    }
    
    public int hashCode(){
        int code = 0;
        
        if (schema_id != null) {code += (schema_id * 10); }
        if (bu_id != null) {code += bu_id; }
        
        return code;
        
    }
}
