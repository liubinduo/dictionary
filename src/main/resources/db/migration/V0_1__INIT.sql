DROP TABLE IF EXISTS "base_table";
CREATE TABLE "base_table"
(
    "create_by"   TEXT,
    "update_by"   TEXT,
    "create_time" TIMESTAMP(0),
    "update_time" TIMESTAMP(0),
    "deleted"     BOOL DEFAULT FALSE,
    "revision"    INT4,
    "ext"         JSONB
)
    WITHOUT OIDS;

DROP TABLE IF EXISTS "districts";
CREATE TABLE "districts"
(
    "pid"       TEXT,
    "city_code" TEXT,
    "ad_code"   TEXT,
    "name"      TEXT,
    "longitude" decimal(10, 6),
    "latitude"  decimal(10, 6),
    "level"     TEXT,
    "parent_id" TEXT,
    PRIMARY KEY ("pid")
)
    WITHOUT OIDS;

comment on column districts.pid is '主键';
comment on column districts.city_code is '区号';
comment on column districts.ad_code is '行政编码';
comment on column districts.name is '名称';
comment on column districts.longitude is '经度';
comment on column districts.latitude is '纬度';
comment on column districts.level is '级别';
comment on column districts.parent_id is '父ID';


