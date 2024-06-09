package com.example.backendl.Model;

import java.util.Date;

public class Friend {
    private String objectId;
    private Date created;
    private String OwnerId;
    private String OwnersFriendId;
    private  String FriendStatus;//підтверджено, відклонено, очікування

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFriendStatus() {
        return FriendStatus;
    }

    public void setFriendStatus(String friendStatus) {
        FriendStatus = friendStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(String ownerId) {
        OwnerId = ownerId;
    }

    public String getOwnersFriendId() {
        return OwnersFriendId;
    }

    public void setOwnersFriendId(String ownersFriendId) {
        OwnersFriendId = ownersFriendId;
    }
}
