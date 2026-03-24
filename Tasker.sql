USE tasker;

CREATE TABLE IF NOT EXISTS tasker.user_credentials(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	username VARCHAR(16) NOT NULL UNIQUE,
	email VARCHAR(32) NOT NULL UNIQUE,
	-- phone VARCHAR(16) NOT NULL UNIQUE,
	password VARCHAR(64) NOT NULL,
	registered_on DATE NULL,
	is_active BOOL NOT NULL
);
ALTER TABLE tasker.user_credentials ADD CONSTRAINT password_length CHECK (char_length(password) >= 4);

INSERT INTO tasker.user_credentials(id, username, email, password, registered_on, is_active)
VALUES(1, 'elkatraz', 'elkatraz@yahoo.com', '$2a$12$gLbhXGSJK3kIZRdrGxBuN.7t3n389M0Uz40J0VppcT0kK0Nf511b2', NOW(), TRUE);

CREATE TABLE IF NOT EXISTS tasker.company(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	name VARCHAR(32) NOT NULL
);

INSERT INTO tasker.company(id, name) VALUES(1, 'Yash Technologies');

CREATE TABLE IF NOT EXISTS tasker.contact_details(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	name VARCHAR(8) NOT NULL,
	address VARCHAR(64) NULL,
	district VARCHAR(16) NULL,
	state VARCHAR(32) NULL,
	country VARCHAR(32) NULL,
	phone_no VARCHAR(16) NULL,
	email VARCHAR(32) NULL,
	company_id BIGINT NULL,
	CONSTRAINT company_contact FOREIGN KEY (company_id) REFERENCES company(id)
);

INSERT INTO tasker.contact_details(id, name, address, district, state, country, phone_no, email, company_id)
VALUES(1, 'Working', '', 'Indore', 'Madhya Pradesh', 'IN', '0', 'elkatraz@yahoo.com', 1);

CREATE TABLE IF NOT EXISTS tasker.user(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	name VARCHAR(32) NOT NULL,
	designation VARCHAR(16) NULL,
	birth_date DATE NULL,
	blood_group VARCHAR(2) NULL,
	user_credentials_id BIGINT NOT NULL UNIQUE,
	CONSTRAINT user_profile FOREIGN KEY (user_credentials_id) REFERENCES user_credentials(id),
	employee_id BIGINT NOT NULL,
	CONSTRAINT employee_in_company FOREIGN KEY (employee_id) REFERENCES company(id),
	temporary_contact_id BIGINT NOT NULL,
	CONSTRAINT temporary_user_contact FOREIGN KEY (temporary_contact_id) REFERENCES contact_details(id),
	permanent_contact_id BIGINT NULL,
	CONSTRAINT permanent_user_contact FOREIGN KEY (permanent_contact_id) REFERENCES contact_details(id)
);

INSERT INTO tasker.user(id, name, designation, birth_date, blood_group, user_credentials_id, employee_id, temporary_contact_id, permanent_contact_id)
VALUES(1, 'Vikas Dubey', 'Don', NOW(), 'O+', 1, 1, 1, 1);

CREATE TABLE IF NOT EXISTS tasker.status(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	label ENUM('PENDING', 'WORKING', 'WAITING', 'COMPLETED') NOT NULL
);
INSERT INTO tasker.status(id, label) VALUES(1, 'PENDING'), (2, 'WORKING'), (3, 'WAITING'), (4, 'COMPLETED');

CREATE TABLE IF NOT EXISTS tasker.priority(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	label ENUM('URGENT', 'MAJOR', 'USUAL', 'MINOR', 'SLIGHT') NOT NULL
);
INSERT INTO tasker.priority(id, label) VALUES(1, 'URGENT'), (2, 'MAJOR'), (3, 'USUAL'), (4, 'MINOR'), (5, 'SLIGHT');

-- CREATE TABLE IF NOT EXISTS tasker.assigned_task(
	-- id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	-- assignee_id BIGINT NOT NULL,
	-- CONSTRAINT subtask_assigner FOREIGN KEY (assignee_id) REFERENCES user_credentials(id),
	-- priority BIGINT NOT NULL,
	-- assinged_date DATE NOT NULL,
	-- assigned_to BIGINT NULL,
	-- CONSTRAINT assigned_task FOREIGN KEY (assigned_to) REFERENCES user_credentials(id)
-- );

CREATE TABLE IF NOT EXISTS tasker.task(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	title VARCHAR(16) NOT NULL,
	description VARCHAR(64) NULL,
	status BIGINT NOT NULL,
	CONSTRAINT task_status FOREIGN KEY (status) REFERENCES status(id),
	assignee_id BIGINT NOT NULL,
	CONSTRAINT task_assigner FOREIGN KEY (assignee_id) REFERENCES user_credentials(id),
	priority BIGINT NOT NULL,
	CONSTRAINT task_priority FOREIGN KEY (priority) REFERENCES priority(id),
	start_date DATE NULL,
	due_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS tasker.sub_task(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	title VARCHAR(16) NOT NULL,
	description VARCHAR(32) NULL,
	task_id BIGINT NOT NULL,
	CONSTRAINT task FOREIGN KEY (task_id) REFERENCES task(id),
	status BIGINT NOT NULL,
	CONSTRAINT sub_task_status FOREIGN KEY (status) REFERENCES status(id),
	priority BIGINT NOT NULL,
	CONSTRAINT sub_task_priority FOREIGN KEY (priority) REFERENCES priority(id),
	assigned_to BIGINT NULL,
	CONSTRAINT sub_task_assign FOREIGN KEY (assigned_to) REFERENCES user_credentials(id),
	assigned_date DATE NULL,
	due_date DATE NOT NULL,
	current_progress INT NOT NULL,
	last_remarks VARCHAR(32) NULL
);

CREATE TABLE IF NOT EXISTS tasker.task_manage(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	task_id BIGINT NOT NULL,
	CONSTRAINT task_manage FOREIGN KEY (task_id) REFERENCES task(id),
	manage_by BIGINT NOT NULL,
	CONSTRAINT task_assigned FOREIGN KEY (assigned_to) REFERENCES user_credentials(id)
);

-- CREATE TABLE IF NOT EXISTS tasker.sub_task_execute(
	-- id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	-- task_id BIGINT NOT NULL,
	-- CONSTRAINT task_managed FOREIGN KEY (task_id) REFERENCES task_manage(id),
	-- manager_id BIGINT NOT NULL,
	-- CONSTRAINT task_manager FOREIGN KEY (manager_id) REFERENCES user_credentials(id)
-- );

CREATE TABLE IF NOT EXISTS tasker.issues(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	task_id BIGINT NOT NULL,
	CONSTRAINT task_issue FOREIGN KEY (task_id) REFERENCES task(id),
	sub_task_id BIGINT NULL,
	CONSTRAINT subtask_issue FOREIGN KEY (sub_task_id) REFERENCES sub_task(id),
	subject VARCHAR(8) NULL,
	description VARCHAR(64) NOT NULL,
	rasied_on DATE NOT NULL,
	resolve_status BOOL NOT NULL
);
ALTER TABLE tasker.issues ADD CONSTRAINT issues_of_task_or_subtask CHECK (((task_id IS NULL) AND (sub_task_id IS NOT NULL)) OR ((task_id IS NOT NULL) AND (sub_task_id IS NULL)));

CREATE TABLE IF NOT EXISTS tasker.feedback(
	id BIGINT PRIMARY KEY NOT NULL UNIQUE AUTO_INCREMENT,
	-- user_credentials_id BIGINT NOT NULL UNIQUE,
	-- CONSTRAINT employee_feedback FOREIGN KEY (user_credentials_id) REFERENCES user_credentials(id),
	-- task_id BIGINT NOT NULL,
	-- CONSTRAINT task_feedback FOREIGN KEY (task_id) REFERENCES task(id),
	sub_task_id BIGINT NOT NULL,
	CONSTRAINT subtask_feedback FOREIGN KEY (sub_task_id) REFERENCES sub_task(id),
	rating TINYINT,
	remarks VARCHAR(32)
);
ALTER TABLE tasker.feedback ADD CONSTRAINT feedback_of_rating_or_remarks CHECK (((rating IS NULL) AND (remarks IS NOT NULL)) OR ((rating IS NOT NULL) AND (remarks IS NULL)));
