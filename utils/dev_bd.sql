INSERT INTO `user_role` (`id`, `label_pt`, `label_en`, `short_label_pt`, `short_label_en`)
VALUES
	(1, 'Utilizador', 'User', 'Utilizador', 'User'),
	(2, 'Administrador', 'Administrator', 'Admin', 'Admin'),
	(3, 'Gestor', 'Manager', 'Gestor', 'Manager'),
	(4, 'Pessoal Técnico', 'Technical Staff', 'Técnico', 'Staff');


INSERT INTO `app_config_application_configuration` (`id`, `application_name_pt`, `application_name_en`, `application_description_pt`, `application_description_en`, `environment`, `base_domain`, `base_url`, `files_path`)
VALUES
	(1, 'Remote Logging App', 'Remote Logging App', 'TODO', 'An app to add worklogs on remote work', 'dev', 'localhost:9292', 'http://localhost:9292/', '/srv/remote-logging-app/files');


INSERT INTO `sys_job_description` (`id`, `name_pt`, `name_en`, `periodicity`, `last_run_start`, `last_run_end`, `next_run`, `is_active`, `is_running`, `has_errors`, version)
VALUES
	(1, 'Verificar buffer de e-mails', 'Check e-mail buffer', 1, NULL, NULL, NOW(), 1, 0, 0, NOW()),
	(2, 'Gerar relatórios', 'Generate reports', 5, NULL, NULL, NOW(), 1, 0, 0, NOW());


INSERT INTO `app_menu_application_menu` (`id`, `parent_id`, `label_pt`, `label_en`, `short_label_pt`, `short_label_en`, `url`, `icon_css_class`, `is_active`, `is_public`, `show_when_authenticated`, `order_within_parent`, `has_divider_before`, `opened_by_default`, `custom_validation_class`)
VALUES
	(1, NULL, 'Menu Topo', 'Top Menu', 'Topo', 'Top', NULL, NULL, 1, 1, 1, 1, 0, 0, NULL),
	(2, NULL, 'Menu de Utilizador', 'User Menu', 'Menu', 'Menu', NULL, 'fa fa-bars', 1, 1, 1, 1, 0, 0, NULL),
	(3, NULL, 'Menu de Backoffice', 'Backoffice Menu', 'Backoffice', 'Backoffice', NULL, NULL, 1, 0, 1, 1, 0, 0, NULL),
	(4, 1, 'Início', 'Home', 'Início', 'Home', '/', 'fa fa-home', 1, 1, 1, 1, 0, 0, NULL),
	(5, 1, 'Estilos', 'Styles', 'Estilos', 'Styles', '/styles', 'fa fa-thumb-tack', 1, 1, 1, 2, 0, 0, NULL),
	(6, 1, 'Testes', 'Tests', 'Testes', 'Tests', '/tests', 'fa fa-wrench', 1, 1, 1, 3, 0, 0, NULL),
	(7, 2, 'Iniciar Sessão', 'Login', 'Entrar', 'Login', '/auth/default-login', 'fa fa-sign-in', 1, 1, 0, 1, 0, 0, NULL),
	(8, 2, 'Área de Backoffice', 'Backoffice Area', 'Backoffice', 'Backoffice', '/bo', 'fa fa-cog', 1, 0, 1, 1, 0, 0, NULL),
	(9, 2, 'Terminar Sessão', 'Logout', 'Sair', 'Logout', '/auth/default-logout', 'fa fa-sign-out', 1, 0, 1, 5, 1, 0, NULL),
	(10, 3, 'Utilizador', 'User', 'Utilizador', 'User', '', 'fa fa-user-circle', 1, 0, 1, 1, 0, 1, ''),
	(11, 3, 'Sistema', 'System', 'Sistema', 'System', '/bo/system', 'fa fa-cogs', 1, 0, 1, 2, 0, 1, ''),
	(12, 10, 'Perfil de Utilizador', 'User Profile', 'Perfil', 'Profile', '/bo/user/profile', 'fa fa-user', 1, 0, 1, 1, 0, 0, NULL),
	(13, 10, 'Mudar de Perfil', 'Change Role', 'Mudar Perfil', 'Change Role', '/bo/user/role-change', 'fa fa-exchange', 1, 0, 1, 2, 0, 0, 'utils.app.menu.RoleChangeMenuValidator'),
	(14, 11, 'Tarefas de Sistema', 'System Jobs', 'Tarefas', 'Jobs', '/bo/system/jobs', 'fa fa-tasks', 1, 0, 1, 2, 0, 0, NULL),
	(15, 11, 'Gestão de Menus', 'Menu Management', 'Menus', 'Menus', '/bo/system/menus', 'fa fa-bars', 1, 0, 1, 4, 0, 0, ''),
	(16, 11, 'Gestão de Utilizadores', 'User Management', 'Utilizadores', 'Users', '/bo/system/users', 'fa fa-group', 1, 0, 1, 5, 0, 0, ''),
	(17, 11, 'Config. Aplicação', 'App. Configuration', 'Config. Apl.', 'App. Config.', '/bo/system/config', 'fa fa-cog', 1, 0, 1, 1, 0, 0, NULL),
	(18, 11, 'Logs de Sistema', 'System Logs', 'Logs Sistema', 'System Logs', '/bo/system/logs', 'fa fa-list', 1, 0, 1, 3, 0, 0, ''),
	(19, 2, 'Terminar Personificação', 'Reset Impersonate', 'Term. Personificação', 'Reset Impersonate', '/bo/system/users/impersonate-reset', 'fa fa-user', 1, 0, 1, 4, 1, 0, 'utils.app.menu.ImpersonateResetMenuValidator'),
	(20, 3, 'Relatórios', 'Reports', 'Relatórios', 'Reports', '', 'fa fa-book', 1, 0, 1, 3, 0, 1, ''),
	(21, 20, 'Relatórios Pedidos', 'Requested Reports', 'Pedidos', 'Requests', '/bo/reports/requests', 'fa fa-table', 1, 0, 1, 3, 0, 0, ''),
	(22, 20, 'Gestão', 'Management', 'Gestão', 'Management', '/bo/reports/management', 'fa fa-cog', 1, 0, 1, 1, 0, 0, ''),
	(23, 20, 'Pedir Relatório', 'Request Report', 'Pedir Relatório', 'Request Report', '/bo/reports/create-request', 'fa fa-plus-square', 1, 0, 1, 2, 0, 0, '');


INSERT INTO `app_menu_application_menu_role` (`menu_id`, `role_id`, `has_access`)
VALUES
	(1, 1, 0),
	(1, 2, 0),
	(1, 3, 0),
	(1, 4, 0),
	(2, 1, 0),
	(2, 2, 0),
	(2, 3, 0),
	(2, 4, 0),
	(3, 1, 1),
	(3, 2, 1),
	(3, 3, 1),
	(3, 4, 1),
	(4, 1, 0),
	(4, 2, 0),
	(4, 3, 0),
	(4, 4, 0),
	(5, 1, 0),
	(5, 2, 0),
	(5, 3, 0),
	(5, 4, 0),
	(6, 1, 0),
	(6, 2, 0),
	(6, 3, 0),
	(6, 4, 0),
	(7, 1, 0),
	(7, 2, 0),
	(7, 3, 0),
	(7, 4, 0),
	(8, 1, 1),
	(8, 2, 1),
	(8, 3, 1),
	(8, 4, 1),
	(9, 1, 1),
	(9, 2, 1),
	(9, 3, 1),
	(9, 4, 1),
	(10, 1, 1),
	(10, 2, 1),
	(10, 3, 1),
	(10, 4, 1),
	(11, 1, 0),
	(11, 2, 1),
	(11, 3, 0),
	(11, 4, 0),
	(12, 1, 1),
	(12, 2, 1),
	(12, 3, 1),
	(12, 4, 1),
	(13, 1, 1),
	(13, 2, 1),
	(13, 3, 1),
	(13, 4, 1),
	(14, 1, 0),
	(14, 2, 1),
	(14, 3, 0),
	(14, 4, 0),
	(15, 1, 0),
	(15, 2, 1),
	(15, 3, 0),
	(15, 4, 0),
	(16, 1, 0),
	(16, 2, 1),
	(16, 3, 0),
	(16, 4, 0),
	(17, 1, 0),
	(17, 2, 1),
	(17, 3, 0),
	(17, 4, 0),
	(18, 1, 0),
	(18, 2, 1),
	(18, 3, 0),
	(18, 4, 0),
	(19, 4, 1),
	(19, 1, 1),
	(19, 2, 1),
	(19, 3, 1),
	(20, 4, 0),
	(20, 1, 0),
	(20, 2, 1),
	(20, 3, 1),
	(21, 4, 0),
	(21, 1, 0),
	(21, 2, 1),
	(21, 3, 1),
	(22, 4, 0),
	(22, 1, 0),
	(22, 2, 1),
	(22, 3, 0),
	(23, 4, 0),
	(23, 1, 0),
	(23, 2, 1),
	(23, 3, 1);


INSERT INTO `sys_redirect_record` (`id`, `original_slug`, `redirect_slug`, `creation_date`, `last_edit_date`)
VALUES
	(1, '/test1', '/test2', '2019-08-27 17:11:00.000000', NULL),
	(2, '/test2', '/tests', '2019-08-27 17:12:00.000000', NULL);



INSERT INTO `app_form_form_element` (`id`, `component`, `element_id`, `parent_id`, `order_within_parent`)
VALUES
	(1, 'form-component', 'f1', NULL, NULL),
	(2, 'row-component', 'r1', 1, 1),
	(3, 'row-component', 'r2', 1, 2),
	(4, 'col-component', 'c1', 2, 1),
	(5, 'col-component', 'c2', 2, 2),
	(6, 'col-component', 'c3', 2, 3),
	(7, 'col-component', 'c4', 3, 1),
	(8, 'input-text-component', 'it1', 4, 1),
	(9, 'input-password-component', 'ip1', 5, 1),
	(10, 'select-component', 's1', 6, 1),
	(11, 'option-component', 'o1', 10, 1),
	(12, 'option-component', 'o2', 10, 2),
	(13, 'option-component', 'o3', 10, 3),
	(14, 'button-component', 'b1', 7, 1),
	(15, 'fa-component', 'fa1', 14, 1),
	(16, 'text-component', 't1', 14, 2);


INSERT INTO `app_form_form_element_property` (`id`, `form_element_id`, `name`, `value`)
VALUES
	(1, 1, 'action', '/tests/form-generator'),
	(2, 1, 'method', 'post'),
	(3, 1, 'css_classes', 'validate-form'),
	(4, 2, 'css_classes', 'mb-3'),
	(5, 3, 'css_classes', 'mb-3'),
	(6, 4, 'grid_css_classes', 'col-md-4 col-sm-6'),
	(7, 5, 'grid_css_classes', 'col-md-3 col-sm-6'),
	(8, 6, 'grid_css_classes', 'col-md-5'),
	(9, 8, 'label_pt', 'Exemplo de input de texto'),
	(10, 8, 'label_en', 'Input text example'),
	(11, 8, 'placeholder_pt', 'Texto placeholder'),
	(12, 8, 'placeholder_en', 'Placeholder text'),
	(13, 8, 'css_classes', 'validate-not-empty validate-blur validate-positive-number'),
	(14, 8, 'name', 'it1'),
	(15, 9, 'name', 'ip1'),
	(16, 9, 'label_pt', 'Exemplo de input de passord'),
	(17, 9, 'label_en', 'Input password example'),
	(18, 9, 'css_classes', 'validate-not-empty validate-blur'),
	(19, 10, 'name', 's1'),
	(20, 10, 'label_pt', 'Exemplo de select'),
	(21, 10, 'label_en', 'Select example'),
	(22, 11, 'value', '1'),
	(23, 11, 'label_pt', 'Opção 1'),
	(24, 11, 'label_en', 'Option 1'),
	(25, 11, 'is_selected', 'false'),
	(26, 12, 'value', '2'),
	(27, 12, 'label_pt', 'Opção 2'),
	(28, 12, 'label_en', 'Option 2'),
	(29, 12, 'is_selected', 'true'),
	(30, 13, 'value', '3'),
	(31, 13, 'label_pt', 'Opção 3'),
	(32, 13, 'label_en', 'Option 3'),
	(33, 13, 'is_selected', 'false'),
	(34, 14, 'type', 'submit'),
	(35, 14, 'theme', 'success'),
	(36, 15, 'icon', 'check-circle'),
	(37, 16, 'text_pt', 'Guardar'),
	(38, 16, 'text_en', 'Save');


INSERT INTO `app_report_report_type` (`id`, `label_pt`, `label_en`, `description_pt`, `description_en`, `report_config_class`, `report_config_helper_class`, `report_execution_class`)
VALUES
	(1, 'Relatório de Utilizadores', 'Users Report', 'Este relatório extrai uma lista dos utilizadores com um determinado perfil. Escolha o perfil em baixo na lista.', 'This report extracts a list of users with a specific role. Choose the role from the list below.', 'utils.app.report.configurations.models.UserReportConfiguration', 'utils.app.report.configurations.helpers.impl.UserReportConfigurationHelper', 'utils.app.report.executions.helpers.impl.UserReportExecutionHelper');

INSERT INTO `app_report_report_type_role` (`report_type_id`, `role_id`, `has_access`)
VALUES
	(1, 1, 0),
	(1, 2, 1),
	(1, 3, 1),
	(1, 4, 0);


INSERT INTO `app_files_resource_associated_file_type` (`id`, `label_pt`, `label_en`, `is_visible`)
VALUES
	(1, 'Foto de Utilizador', 'User Photo', 1),
	(2, 'Relatório', 'Report', 1);
