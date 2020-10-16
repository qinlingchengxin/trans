使用API方式传输当原数据库为SQL SERVER 时，版本需要 >= SQL SERVER 2012,页面暂时没开放

说明：
    1、如果是用ETL进行传输，则需要建完任务，添加表映射、字段映射，执行etl传输即可
    2、如果是用API方式进行传输，则需要部署两套此应用，并且两套应用对应的数据表接口完全一样，在建立任务时，数据源应该一样，这样便于添加映射表和字段
       在远程应用中在进行ETL传输，这样实现字段不一致的情况，修改config.properties文件中的接口地址。如果只是为了传输数据，则不需要在配置ETL传输了
    3、使用API方式，数据源需要对传输的原表和目标表均添加系统时间戳字段，sql命令如下，只需替换表名：
        mysql:
            添加字段：
                ALTER TABLE `person` ADD COLUMN `SYS__CREATE_OR_UPDATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP() ON UPDATE CURRENT_TIMESTAMP;
            添加索引：
                ALTER TABLE `person` ADD INDEX `idx_person_scout` (`SYS__CREATE_OR_UPDATE_TIME`) USING BTREE ;

        oracle:
            添加字段：ALTER TABLE "PERSON" ADD ( "SYS__CREATE_OR_UPDATE_TIME" TIMESTAMP(0)  DEFAULT sysdate NULL);
            添加索引：CREATE INDEX "idx_person_scout" ON "PERSON" ("SYS__CREATE_OR_UPDATE_TIME" ASC);
            添加触发器：CREATE OR REPLACE TRIGGER trigger_name BEFORE UPDATE ON PERSON FOR EACH ROW BEGIN :NEW.SYS__CREATE_OR_UPDATE_TIME := SYSDATE ; END ;

    4、目前对Oracle和Mysql支持良好，对Sql server不良好

    5、maven私库需要添加kettle的相关私库镜像：
        http://nexus.pentaho.org/content/groups/omni/

    6、需要将kettle.properties文件放到home目录的.kettle文件夹下，否则不起作用，待研究