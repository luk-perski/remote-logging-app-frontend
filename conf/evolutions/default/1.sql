# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table app_config_application_configuration (
  id                            integer auto_increment not null,
  application_name_pt           varchar(50) not null,
  application_name_en           varchar(50) not null,
  application_description_pt    varchar(200),
  application_description_en    varchar(200),
  environment                   varchar(10) not null,
  base_domain                   varchar(100) not null,
  base_url                      varchar(100) not null,
  files_path                    varchar(255) not null,
  constraint uq_app_config_application_configuration_application_name_pt unique (application_name_pt),
  constraint uq_app_config_application_configuration_application_name_en unique (application_name_en),
  constraint uq_app_config_application_configuration_environment unique (environment),
  constraint uq_app_config_application_configuration_base_domain unique (base_domain),
  constraint uq_app_config_application_configuration_base_url unique (base_url),
  constraint uq_app_config_application_configuration_files_path unique (files_path),
  constraint pk_app_config_application_configuration primary key (id)
);

create table app_config_application_configuration_property (
  id                            integer auto_increment not null,
  application_configuration_id  integer,
  label                         varchar(100) not null,
  value                         varchar(500) not null,
  constraint pk_app_config_application_configuration_property primary key (id)
);

create table log_application_log (
  id                            bigint auto_increment not null,
  class_name                    varchar(250) not null,
  instant                       DATETIME DEFAULT NOW() not null,
  context                       varchar(50) not null,
  data                          TEXT,
  constraint pk_log_application_log primary key (id)
);

create table app_menu_application_menu (
  id                            integer auto_increment not null,
  parent_id                     integer,
  label_pt                      varchar(50) not null,
  label_en                      varchar(50) not null,
  short_label_pt                varchar(30) not null,
  short_label_en                varchar(30) not null,
  url                           varchar(200),
  icon_css_class                varchar(255),
  is_active                     TINYINT DEFAULT 0 not null,
  is_public                     TINYINT DEFAULT 0 not null,
  show_when_authenticated       TINYINT DEFAULT 0 not null,
  order_within_parent           INT DEFAULT 1 not null,
  has_divider_before            TINYINT DEFAULT 0 not null,
  opened_by_default             TINYINT DEFAULT 0 not null,
  custom_validation_class       varchar(250),
  constraint pk_app_menu_application_menu primary key (id)
);

create table app_menu_application_menu_role (
  id                            integer auto_increment not null,
  menu_id                       integer,
  role_id                       integer,
  has_access                    TINYINT DEFAULT 0 not null,
  constraint pk_app_menu_application_menu_role primary key (id)
);

create table app_form_form_element (
  id                            bigint auto_increment not null,
  component                     varchar(255) not null,
  element_id                    varchar(255),
  parent_id                     bigint,
  order_within_parent           integer,
  constraint pk_app_form_form_element primary key (id)
);

create table app_form_form_element_property (
  id                            bigint auto_increment not null,
  form_element_id               bigint,
  name                          varchar(255) not null,
  value                         varchar(255) not null,
  constraint pk_app_form_form_element_property primary key (id)
);

create table sys_job_description (
  id                            integer auto_increment not null,
  name_pt                       varchar(200) not null,
  name_en                       varchar(200) not null,
  periodicity                   INT DEFAULT 0 not null,
  last_run_start                datetime(6),
  last_run_end                  datetime(6),
  last_run_log                  MEDIUMTEXT,
  next_run                      datetime(6),
  is_active                     TINYINT DEFAULT 0 not null,
  is_running                    TINYINT DEFAULT 0 not null,
  has_errors                    TINYINT DEFAULT 0 not null,
  version                       datetime(6) not null,
  constraint uq_sys_job_description_name_pt unique (name_pt),
  constraint uq_sys_job_description_name_en unique (name_en),
  constraint pk_sys_job_description primary key (id)
);

create table remote_logging_project (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  manager_id                    bigint not null,
  is_active                     tinyint(1) default 0,
  description                   varchar(255),
  crated_date                   DATETIME DEFAULT NOW() not null,
  start_date                    datetime(6),
  end_date                      datetime(6),
  constraint pk_remote_logging_project primary key (id)
);

create table sys_redirect_record (
  id                            bigint auto_increment not null,
  original_slug                 varchar(1000) not null,
  redirect_slug                 varchar(1000) not null,
  creation_date                 DATETIME DEFAULT NOW() not null,
  last_edit_date                datetime(6),
  constraint uq_sys_redirect_record_original_slug unique (original_slug),
  constraint uq_sys_redirect_record_redirect_slug unique (redirect_slug),
  constraint pk_sys_redirect_record primary key (id)
);

create table app_report_report_execution (
  id                            bigint auto_increment not null,
  report_request_id             bigint,
  execution_date_begin          DATETIME DEFAULT NOW() not null,
  execution_date_end            DATETIME DEFAULT NOW() not null,
  execution_log                 TEXT,
  constraint pk_app_report_report_execution primary key (id)
);

create table app_report_report_request (
  id                            bigint auto_increment not null,
  report_type_id                integer,
  request_user_id               bigint,
  request_date                  DATETIME DEFAULT NOW() not null,
  is_recurrent                  TINYINT DEFAULT 0 not null,
  periodicity                   integer,
  configuration                 TEXT,
  is_active                     TINYINT DEFAULT 1 not null,
  is_executing                  TINYINT DEFAULT 0 not null,
  constraint pk_app_report_report_request primary key (id)
);

create table app_report_report_type (
  id                            integer auto_increment not null,
  label_pt                      varchar(255) not null,
  label_en                      varchar(255) not null,
  description_pt                varchar(1000) not null,
  description_en                varchar(1000) not null,
  report_config_class           varchar(255),
  report_config_helper_class    varchar(255),
  report_execution_class        varchar(255) not null,
  is_active                     TINYINT DEFAULT 1 not null,
  constraint pk_app_report_report_type primary key (id)
);

create table app_report_report_type_role (
  id                            integer auto_increment not null,
  report_type_id                integer,
  role_id                       integer,
  has_access                    TINYINT DEFAULT 0 not null,
  constraint pk_app_report_report_type_role primary key (id)
);

create table app_files_resource_associated_file (
  id                            bigint auto_increment not null,
  resource_id                   varchar(20),
  resource_class                varchar(250),
  file_type_id                  integer,
  file_name                     varchar(255) not null,
  file_path                     varchar(1000) not null,
  file_hash                     varchar(32) not null,
  file_size                     bigint not null,
  file_content_type             varchar(255),
  is_public                     TINYINT DEFAULT 0 not null,
  restricted_access_validation_class varchar(255),
  restricted_access_validation_data TEXT,
  record_accesses               TINYINT DEFAULT 0 not null,
  constraint pk_app_files_resource_associated_file primary key (id)
);

create table app_files_resource_associated_file_access_record (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  resource_associated_file_id   bigint,
  accessed_date                 DATETIME DEFAULT NOW() not null,
  constraint pk_app_files_resource_associated_file_access_record primary key (id)
);

create table app_files_resource_associated_file_type (
  id                            integer auto_increment not null,
  label_pt                      varchar(255) not null,
  label_en                      varchar(255) not null,
  is_visible                    TINYINT DEFAULT 0 not null,
  constraint pk_app_files_resource_associated_file_type primary key (id)
);

create table user_role (
  id                            integer auto_increment not null,
  label_pt                      varchar(100) not null,
  label_en                      varchar(100) not null,
  short_label_pt                varchar(10) not null,
  short_label_en                varchar(10) not null,
  constraint uq_user_role_label_pt unique (label_pt),
  constraint uq_user_role_label_en unique (label_en),
  constraint uq_user_role_short_label_pt unique (short_label_pt),
  constraint uq_user_role_short_label_en unique (short_label_en),
  constraint pk_user_role primary key (id)
);

create table search_search_content_class_custom_handler (
  content_class                 varchar(200) not null,
  custom_handler_class          varchar(200),
  constraint pk_search_search_content_class_custom_handler primary key (content_class)
);

create table search_search_content_prepared (
  id                            bigint auto_increment not null,
  content_id                    varchar(20) not null,
  content_class                 varchar(200) not null,
  title                         varchar(2000) not null,
  slug                          varchar(200) not null,
  published                     tinyint(1) not null,
  publication_start             datetime(6),
  publication_end               datetime(6),
  submitted                     datetime(6),
  s1                            MEDIUMTEXT,
  s2                            TEXT,
  constraint pk_search_search_content_prepared primary key (id)
);

create table search_search_content_ranked (
  id                            bigint auto_increment not null,
  search                        varchar(255),
  content_prepared_id           bigint,
  hits                          bigint,
  constraint pk_search_search_content_ranked primary key (id)
);

create table math_sum (
  id                            bigint auto_increment not null,
  number_one                    integer not null,
  number_two                    integer not null,
  result                        integer not null,
  instant                       DATETIME DEFAULT NOW() not null,
  constraint pk_math_sum primary key (id)
);

create table sys_system_buffered_email (
  id                            bigint auto_increment not null,
  from_address                  varchar(100) not null,
  recipients                    varchar(200) not null,
  subject                       varchar(255),
  plain_message                 TEXT,
  html_message                  TEXT,
  date_buffered                 DATETIME DEFAULT NOW() not null,
  urgency                       INT DEFAULT 0 not null,
  constraint pk_sys_system_buffered_email primary key (id)
);

create table sys_system_error_email (
  id                            bigint auto_increment not null,
  from_address                  varchar(100) not null,
  recipients                    varchar(200) not null,
  subject                       varchar(255),
  plain_message                 TEXT,
  html_message                  TEXT,
  date_buffered                 datetime(6),
  date_error                    DATETIME DEFAULT NOW() not null,
  error_message                 varchar(500),
  constraint pk_sys_system_error_email primary key (id)
);

create table sys_system_sent_email (
  id                            bigint auto_increment not null,
  from_address                  varchar(100) not null,
  recipients                    varchar(200) not null,
  subject                       varchar(255),
  plain_message                 TEXT,
  html_message                  TEXT,
  date_buffered                 datetime(6),
  date_sent                     DATETIME DEFAULT NOW() not null,
  sent_message_id               varchar(255),
  constraint pk_sys_system_sent_email primary key (id)
);

create table remote_logging_task (
  id                            bigint auto_increment not null,
  project_id                    bigint not null,
  name                          varchar(255) not null,
  creator_id                    bigint not null,
  priority                      integer not null,
  assignee_id                   bigint,
  category                      integer not null,
  crated_date                   DATETIME DEFAULT NOW() not null,
  description                   varchar(255),
  estimate                      bigint,
  resolved_date                 datetime(6),
  time_spent                    bigint,
  run_start                     datetime(6),
  run_end                       datetime(6),
  constraint pk_remote_logging_task primary key (id)
);

create table remote_logging_team (
  id                            bigint auto_increment not null,
  name                          varchar(255) not null,
  manager_id                    bigint not null,
  created_date                  DATETIME DEFAULT NOW() not null,
  constraint pk_remote_logging_team primary key (id)
);

create table user_user (
  id                            bigint auto_increment not null,
  name                          varchar(250) not null,
  display_name                  varchar(250),
  username                      varchar(50) not null,
  email                         varchar(100) not null,
  local_pwd                     varchar(255),
  team_id                       bigint,
  constraint uq_user_user_username unique (username),
  constraint uq_user_user_email unique (email),
  constraint pk_user_user primary key (id)
);

create table log_user_log (
  id                            bigint auto_increment not null,
  class_name                    varchar(250) not null,
  user_id                       bigint,
  instant                       DATETIME DEFAULT NOW() not null,
  data                          TEXT,
  constraint pk_log_user_log primary key (id)
);

create table user_user_oauth (
  id                            varchar(50) not null,
  user_id                       bigint,
  last_updated                  datetime(6),
  data_processor                varchar(250),
  data                          TEXT,
  constraint pk_user_user_oauth primary key (id)
);

create table user_user_role (
  id                            bigint auto_increment not null,
  user_id                       bigint,
  role_id                       integer,
  is_active                     TINYINT DEFAULT 0 not null,
  constraint pk_user_user_role primary key (id)
);

create index ix_log_application_log_instant on log_application_log (instant);
create index ix_log_application_log_context on log_application_log (context);
create index ix_app_files_resource_associated_file_resource_id on app_files_resource_associated_file (resource_id);
create index ix_app_files_resource_associated_file_file_hash on app_files_resource_associated_file (file_hash);
create index ix_user_user_username on user_user (username);
create index ix_log_user_log_instant on log_user_log (instant);
create index ix_app_config_application_configuration_property_applicat_1 on app_config_application_configuration_property (application_configuration_id);
alter table app_config_application_configuration_property add constraint fk_app_config_application_configuration_property_applicat_1 foreign key (application_configuration_id) references app_config_application_configuration (id) on delete restrict on update restrict;

create index ix_app_menu_application_menu_parent_id on app_menu_application_menu (parent_id);
alter table app_menu_application_menu add constraint fk_app_menu_application_menu_parent_id foreign key (parent_id) references app_menu_application_menu (id) on delete restrict on update restrict;

create index ix_app_menu_application_menu_role_menu_id on app_menu_application_menu_role (menu_id);
alter table app_menu_application_menu_role add constraint fk_app_menu_application_menu_role_menu_id foreign key (menu_id) references app_menu_application_menu (id) on delete restrict on update restrict;

create index ix_app_menu_application_menu_role_role_id on app_menu_application_menu_role (role_id);
alter table app_menu_application_menu_role add constraint fk_app_menu_application_menu_role_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;

create index ix_app_form_form_element_parent_id on app_form_form_element (parent_id);
alter table app_form_form_element add constraint fk_app_form_form_element_parent_id foreign key (parent_id) references app_form_form_element (id) on delete restrict on update restrict;

create index ix_app_form_form_element_property_form_element_id on app_form_form_element_property (form_element_id);
alter table app_form_form_element_property add constraint fk_app_form_form_element_property_form_element_id foreign key (form_element_id) references app_form_form_element (id) on delete restrict on update restrict;

create index ix_remote_logging_project_manager_id on remote_logging_project (manager_id);
alter table remote_logging_project add constraint fk_remote_logging_project_manager_id foreign key (manager_id) references user_user (id) on delete restrict on update restrict;

create index ix_app_report_report_execution_report_request_id on app_report_report_execution (report_request_id);
alter table app_report_report_execution add constraint fk_app_report_report_execution_report_request_id foreign key (report_request_id) references app_report_report_request (id) on delete restrict on update restrict;

create index ix_app_report_report_request_report_type_id on app_report_report_request (report_type_id);
alter table app_report_report_request add constraint fk_app_report_report_request_report_type_id foreign key (report_type_id) references app_report_report_type (id) on delete restrict on update restrict;

create index ix_app_report_report_request_request_user_id on app_report_report_request (request_user_id);
alter table app_report_report_request add constraint fk_app_report_report_request_request_user_id foreign key (request_user_id) references user_user (id) on delete restrict on update restrict;

create index ix_app_report_report_type_role_report_type_id on app_report_report_type_role (report_type_id);
alter table app_report_report_type_role add constraint fk_app_report_report_type_role_report_type_id foreign key (report_type_id) references app_report_report_type (id) on delete restrict on update restrict;

create index ix_app_report_report_type_role_role_id on app_report_report_type_role (role_id);
alter table app_report_report_type_role add constraint fk_app_report_report_type_role_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;

create index ix_app_files_resource_associated_file_file_type_id on app_files_resource_associated_file (file_type_id);
alter table app_files_resource_associated_file add constraint fk_app_files_resource_associated_file_file_type_id foreign key (file_type_id) references app_files_resource_associated_file_type (id) on delete restrict on update restrict;

create index ix_app_files_resource_associated_file_access_record_user_id on app_files_resource_associated_file_access_record (user_id);
alter table app_files_resource_associated_file_access_record add constraint fk_app_files_resource_associated_file_access_record_user_id foreign key (user_id) references user_user (id) on delete restrict on update restrict;

create index ix_app_files_resource_associated_file_access_record_resou_2 on app_files_resource_associated_file_access_record (resource_associated_file_id);
alter table app_files_resource_associated_file_access_record add constraint fk_app_files_resource_associated_file_access_record_resou_2 foreign key (resource_associated_file_id) references app_files_resource_associated_file (id) on delete restrict on update restrict;

create index ix_search_search_content_ranked_content_prepared_id on search_search_content_ranked (content_prepared_id);
alter table search_search_content_ranked add constraint fk_search_search_content_ranked_content_prepared_id foreign key (content_prepared_id) references search_search_content_prepared (id) on delete restrict on update restrict;

create index ix_remote_logging_task_project_id on remote_logging_task (project_id);
alter table remote_logging_task add constraint fk_remote_logging_task_project_id foreign key (project_id) references remote_logging_project (id) on delete restrict on update restrict;

create index ix_remote_logging_task_creator_id on remote_logging_task (creator_id);
alter table remote_logging_task add constraint fk_remote_logging_task_creator_id foreign key (creator_id) references user_user (id) on delete restrict on update restrict;

create index ix_remote_logging_task_assignee_id on remote_logging_task (assignee_id);
alter table remote_logging_task add constraint fk_remote_logging_task_assignee_id foreign key (assignee_id) references user_user (id) on delete restrict on update restrict;

create index ix_remote_logging_team_manager_id on remote_logging_team (manager_id);
alter table remote_logging_team add constraint fk_remote_logging_team_manager_id foreign key (manager_id) references user_user (id) on delete restrict on update restrict;

create index ix_user_user_team_id on user_user (team_id);
alter table user_user add constraint fk_user_user_team_id foreign key (team_id) references remote_logging_team (id) on delete restrict on update restrict;

create index ix_log_user_log_user_id on log_user_log (user_id);
alter table log_user_log add constraint fk_log_user_log_user_id foreign key (user_id) references user_user (id) on delete restrict on update restrict;

create index ix_user_user_oauth_user_id on user_user_oauth (user_id);
alter table user_user_oauth add constraint fk_user_user_oauth_user_id foreign key (user_id) references user_user (id) on delete restrict on update restrict;

create index ix_user_user_role_user_id on user_user_role (user_id);
alter table user_user_role add constraint fk_user_user_role_user_id foreign key (user_id) references user_user (id) on delete restrict on update restrict;

create index ix_user_user_role_role_id on user_user_role (role_id);
alter table user_user_role add constraint fk_user_user_role_role_id foreign key (role_id) references user_role (id) on delete restrict on update restrict;


# --- !Downs

alter table app_config_application_configuration_property drop foreign key fk_app_config_application_configuration_property_applicat_1;
drop index ix_app_config_application_configuration_property_applicat_1 on app_config_application_configuration_property;

alter table app_menu_application_menu drop foreign key fk_app_menu_application_menu_parent_id;
drop index ix_app_menu_application_menu_parent_id on app_menu_application_menu;

alter table app_menu_application_menu_role drop foreign key fk_app_menu_application_menu_role_menu_id;
drop index ix_app_menu_application_menu_role_menu_id on app_menu_application_menu_role;

alter table app_menu_application_menu_role drop foreign key fk_app_menu_application_menu_role_role_id;
drop index ix_app_menu_application_menu_role_role_id on app_menu_application_menu_role;

alter table app_form_form_element drop foreign key fk_app_form_form_element_parent_id;
drop index ix_app_form_form_element_parent_id on app_form_form_element;

alter table app_form_form_element_property drop foreign key fk_app_form_form_element_property_form_element_id;
drop index ix_app_form_form_element_property_form_element_id on app_form_form_element_property;

alter table remote_logging_project drop foreign key fk_remote_logging_project_manager_id;
drop index ix_remote_logging_project_manager_id on remote_logging_project;

alter table app_report_report_execution drop foreign key fk_app_report_report_execution_report_request_id;
drop index ix_app_report_report_execution_report_request_id on app_report_report_execution;

alter table app_report_report_request drop foreign key fk_app_report_report_request_report_type_id;
drop index ix_app_report_report_request_report_type_id on app_report_report_request;

alter table app_report_report_request drop foreign key fk_app_report_report_request_request_user_id;
drop index ix_app_report_report_request_request_user_id on app_report_report_request;

alter table app_report_report_type_role drop foreign key fk_app_report_report_type_role_report_type_id;
drop index ix_app_report_report_type_role_report_type_id on app_report_report_type_role;

alter table app_report_report_type_role drop foreign key fk_app_report_report_type_role_role_id;
drop index ix_app_report_report_type_role_role_id on app_report_report_type_role;

alter table app_files_resource_associated_file drop foreign key fk_app_files_resource_associated_file_file_type_id;
drop index ix_app_files_resource_associated_file_file_type_id on app_files_resource_associated_file;

alter table app_files_resource_associated_file_access_record drop foreign key fk_app_files_resource_associated_file_access_record_user_id;
drop index ix_app_files_resource_associated_file_access_record_user_id on app_files_resource_associated_file_access_record;

alter table app_files_resource_associated_file_access_record drop foreign key fk_app_files_resource_associated_file_access_record_resou_2;
drop index ix_app_files_resource_associated_file_access_record_resou_2 on app_files_resource_associated_file_access_record;

alter table search_search_content_ranked drop foreign key fk_search_search_content_ranked_content_prepared_id;
drop index ix_search_search_content_ranked_content_prepared_id on search_search_content_ranked;

alter table remote_logging_task drop foreign key fk_remote_logging_task_project_id;
drop index ix_remote_logging_task_project_id on remote_logging_task;

alter table remote_logging_task drop foreign key fk_remote_logging_task_creator_id;
drop index ix_remote_logging_task_creator_id on remote_logging_task;

alter table remote_logging_task drop foreign key fk_remote_logging_task_assignee_id;
drop index ix_remote_logging_task_assignee_id on remote_logging_task;

alter table remote_logging_team drop foreign key fk_remote_logging_team_manager_id;
drop index ix_remote_logging_team_manager_id on remote_logging_team;

alter table user_user drop foreign key fk_user_user_team_id;
drop index ix_user_user_team_id on user_user;

alter table log_user_log drop foreign key fk_log_user_log_user_id;
drop index ix_log_user_log_user_id on log_user_log;

alter table user_user_oauth drop foreign key fk_user_user_oauth_user_id;
drop index ix_user_user_oauth_user_id on user_user_oauth;

alter table user_user_role drop foreign key fk_user_user_role_user_id;
drop index ix_user_user_role_user_id on user_user_role;

alter table user_user_role drop foreign key fk_user_user_role_role_id;
drop index ix_user_user_role_role_id on user_user_role;

drop table if exists app_config_application_configuration;

drop table if exists app_config_application_configuration_property;

drop table if exists log_application_log;

drop table if exists app_menu_application_menu;

drop table if exists app_menu_application_menu_role;

drop table if exists app_form_form_element;

drop table if exists app_form_form_element_property;

drop table if exists sys_job_description;

drop table if exists remote_logging_project;

drop table if exists sys_redirect_record;

drop table if exists app_report_report_execution;

drop table if exists app_report_report_request;

drop table if exists app_report_report_type;

drop table if exists app_report_report_type_role;

drop table if exists app_files_resource_associated_file;

drop table if exists app_files_resource_associated_file_access_record;

drop table if exists app_files_resource_associated_file_type;

drop table if exists user_role;

drop table if exists search_search_content_class_custom_handler;

drop table if exists search_search_content_prepared;

drop table if exists search_search_content_ranked;

drop table if exists math_sum;

drop table if exists sys_system_buffered_email;

drop table if exists sys_system_error_email;

drop table if exists sys_system_sent_email;

drop table if exists remote_logging_task;

drop table if exists remote_logging_team;

drop table if exists user_user;

drop table if exists log_user_log;

drop table if exists user_user_oauth;

drop table if exists user_user_role;

drop index ix_log_application_log_instant on log_application_log;
drop index ix_log_application_log_context on log_application_log;
drop index ix_app_files_resource_associated_file_resource_id on app_files_resource_associated_file;
drop index ix_app_files_resource_associated_file_file_hash on app_files_resource_associated_file;
drop index ix_user_user_username on user_user;
drop index ix_log_user_log_instant on log_user_log;
