package fr.soat.socialnetwork.bo;

import java.util.Date;
import java.util.List;

public interface IDiscussion {

	public abstract String getSubject();

	public abstract void setSubject(String subject);

	public abstract IUser getCreatedBy();

	public abstract void setCreatedBy(IUser createdBy);

	public abstract Date getCreationTime();

	public abstract void setCreationTime(Date creationTime);

	public abstract IGroup getGroup();

	public abstract void setGroup(IGroup group);

	public abstract List<IPost> getPosts();

	public abstract void setPosts(List<IPost> posts);

	public abstract IUser getUpdatedBy();

	public abstract void setUpdatedBy(IUser updatedBy);

	public abstract Date getUpdateTime();

	public abstract void setUpdateTime(Date updateTime);

	public Integer getId();

	public void setId(Integer id);

}