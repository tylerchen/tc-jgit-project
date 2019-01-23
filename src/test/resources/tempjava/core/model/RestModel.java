/*******************************************************************************
 * Copyright (c) 2018-12-05 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import javax.validation.constraints.NotNull;
import java.beans.Transient;

/**
 * RestModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-12-05
 * auto generate by qdp.
 */
public class RestModel extends DomainModel<RestModel> {
    public RestModel() {
    }

    @Override
    public RestModel newInstance() {
        return getClass() == RestModel.class ? new RestModel() : super.newInstance();
    }

    @Override
    public RestModel init(@NotNull String namespace, @NotNull String modelName) {
        super.init(namespace, modelName);
        return this;
    }

    @Transient
    public String getId() {
        return getStr("ID");
    }

    @Transient
    public void setId(String id) {
        put("ID", id);
    }

    @Transient
    public String getNamespace() {
        return (String) get("NAMESPACE");
    }

    @Transient
    public void setNamespace(String namespace) {
        put("NAMESPACE", namespace);
    }

    @Transient
    public String getModelName() {
        return (String) get("MODEL_NAME");
    }

    @Transient
    public void setModelName(String modelName) {
        put("MODEL_NAME", modelName);
    }

    @Transient
    public String getRestName() {
        return (String) get("REST_NAME");
    }

    @Transient
    public void setRestName(String restName) {
        put("REST_NAME", restName);
    }

    @Transient
    public String getRestPath() {
        return (String) get("REST_PATH");
    }

    @Transient
    public void setRestPath(String restPath) {
        put("REST_PATH", restPath);
    }

    @Transient
    public String getIsPublic() {
        return (String) get("IS_PUBLIC");
    }

    @Transient
    public void setIsPublic(String isPublic) {
        put("IS_PUBLIC", isPublic);
    }

    @Transient
    public String getEncryptType() {
        return (String) get("ENCRYPT_TYPE");
    }

    @Transient
    public void setEncryptType(String encryptType) {
        put("ENCRYPT_TYPE", encryptType);
    }

    @Transient
    public String getEncryptKey() {
        return (String) get("ENCRYPT_KEY");
    }

    @Transient
    public void setEncryptKey(String encryptKey) {
        put("ENCRYPT_KEY", encryptKey);
    }

    @Transient
    public String getDecryptType() {
        return (String) get("DECRYPT_TYPE");
    }

    @Transient
    public void setDecryptType(String decryptType) {
        put("DECRYPT_TYPE", decryptType);
    }

    @Transient
    public String getDecryptKey() {
        return (String) get("DECRYPT_KEY");
    }

    @Transient
    public void setDecryptKey(String decryptKey) {
        put("DECRYPT_KEY", decryptKey);
    }

    @Transient
    public String getSignType() {
        return (String) get("SIGN_TYPE");
    }

    @Transient
    public void setSignType(String signType) {
        put("SIGN_TYPE", signType);
    }

    @Transient
    public String getSignPriKey() {
        return (String) get("SIGN_PRI_KEY");
    }

    @Transient
    public void setSignPriKey(String signPriKey) {
        put("SIGN_PRI_KEY", signPriKey);
    }

    @Transient
    public String getSignPubKey() {
        return (String) get("SIGN_PUB_KEY");
    }

    @Transient
    public void setSignPubKey(String signPubKey) {
        put("SIGN_PUB_KEY", signPubKey);
    }

    @Transient
    public String getDescription() {
        return (String) get("DESCRIPTION");
    }

    @Transient
    public void setDescription(String description) {
        put("DESCRIPTION", description);
    }

    @Transient
    public String getCreateTime() {
        return (String) get("CREATE_TIME");
    }

    @Transient
    public void setCreateTime(String createTime) {
        put("CREATE_TIME", createTime);
    }

    @Transient
    public String getUpateTime() {
        return (String) get("UPDATE_TIME");
    }

    @Transient
    public void setUpateTime(String upateTime) {
        put("UPDATE_TIME", upateTime);
    }
}
