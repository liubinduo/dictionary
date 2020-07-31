package com.v1ok.dictionary.db.model;

import com.v1ok.db.model.IEntityModel;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "districts")
public class DistrictsEntity implements IEntityModel {

  @Id
  @Column(name = "pid")
  private String pid;

  @Basic
  @Column(name = "city_code")
  private String cityCode;

  @Basic
  @Column(name = "ad_code")
  private String adCode;

  @Basic
  @Column(name = "name")
  private String name;

  @Basic
  @Column(name = "longitude")
  private Double longitude;

  @Basic
  @Column(name = "latitude")
  private Double latitude;

  @Basic
  @Column(name = "level")
  private String level;

  @Basic
  @Column(name = "parent_id")
  private String parentId;

  public DistrictsEntity() {
  }


  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (o instanceof DistrictsEntity) {
      return EqualsBuilder.reflectionEquals(this, o, true);
    }

    return false;
  }


  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, true);
  }
}
