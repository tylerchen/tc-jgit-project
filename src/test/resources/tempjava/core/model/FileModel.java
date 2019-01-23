/*******************************************************************************
 * Copyright (c) 2018-11-21 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation.
 * Auto Generate By foreveross.com Quick Deliver Platform. 
 ******************************************************************************/
package org.iff.jgit.core.model;

import java.beans.Transient;

/**
 * FileModel
 *
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since 2018-11-21
 * auto generate by qdp.
 */
public class FileModel extends DomainModel<FileModel> {
    @Override
    public FileModel newInstance() {
        return getClass() == FileModel.class ? new FileModel() : super.newInstance();
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
    public String getFileType() {
        return getStr("FILE_TYPE");
    }

    @Transient
    public void setFileType(String fileType) {
        put("FILE_TYPE", fileType);
    }

    @Transient
    public String getIsEncoded() {
        return getStr("IS_ENCODED");
    }

    @Transient
    public void setIsEncoded(String isEncoded) {
        put("IS_ENCODED", isEncoded);
    }

    @Transient
    public String getContentType() {
        return getStr("CONTENT_TYPE");
    }

    @Transient
    public void setContentType(String contentType) {
        put("CONTENT_TYPE", contentType);
    }

    @Transient
    public String getFileName() {
        return getStr("FILE_NAME");
    }

    @Transient
    public void setFileName(String fileName) {
        put("FILE_NAME", fileName);
    }

    @Transient
    public Long getFileSize() {
        Number fileSize = getNumber("FILE_SIZE");
        return fileSize == null ? 0 : fileSize.longValue();
    }

    @Transient
    public void setFileSize(Long fileSize) {
        put("FILE_SIZE", fileSize);
    }

    @Transient
    public String getContent() {
        return getStr("CONTENT");
    }

    @Transient
    public void setContent(String content) {
        put("CONTENT", content);
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
    public String getUpdateTime() {
        return (String) get("UPDATE_TIME");
    }

    @Transient
    public void setUpdateTime(String updateTime) {
        put("UPDATE_TIME", updateTime);
    }
}
