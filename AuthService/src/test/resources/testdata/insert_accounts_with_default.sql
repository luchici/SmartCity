--default account has USER and ADMIN roles
--by default password are encode by BCrypt 12
--5 accounts with USER and ADMIN roles
--5 accounts with ADMIN role
--16 accounts with USER role

-- Password is '12345'
INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (26,'DavidTheBeast', '$2a$12$v8RF2XlhFPtN4gaKsf89D.L5f/K5volP1EVPfHuAHM.FuN4fAm49S', 'David', 'Abdullah', 'Jerusalem', '1977-07-23', 'david@gmail.com', true, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(26, 0);
INSERT INTO account_role(account, role_id)
VALUES(26, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (1,'example_bug', '$2a$12$tnW4wL.pKQaFGZIz6713EOO/GBVIWSZBMB1f2HO8WP7pUCkdZ7lsu', 'Crystal', 'Howells', 'Grebridge', '2005-01-01', 'bmg1970@agung001.com', false, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(1, 0);
INSERT INTO account_role(account, role_id)
VALUES(1, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (2,'boozer_example', '$2a$12$FiH0Dz/A16QAQDRqYqQQ0eQuLloei3RgGBh7663OTFaCze3yVJ6X2', 'Lia', 'Warner', 'Srarset', '1986-07-02', 'zhyla1970@sandra2034.boats', false, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(2, 0);
INSERT INTO account_role(account, role_id)
VALUES(2, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (3,'hight_example', '$2a$12$rwha5QMBAaokmCM2Dm6Imu87/YKYZG5lMFWbCgQJQ.rKa/eUPNOzS', 'Meghan', 'King', 'Proukstin', '1984-04-04', 'roccraz@haicaotv2.com', false, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(3, 0);
INSERT INTO account_role(account, role_id)
VALUES(3, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (4,'example_chessie', '$2a$12$63T5Id3HTAC6YfsbqN3kuuRKGX59wGEhkDPgAbP27job/qtkGpZ2K', 'Tina', 'Bright', 'Vlusa', '1997-05-06', '66666699@namesloz.com', true, false, true, true);
INSERT INTO account_role(account, role_id)
VALUES(4, 0);
INSERT INTO account_role(account, role_id)
VALUES(4, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (5,'bugsy_example', '$2a$12$rUUiE3/a9iUgc81tE4K8F.ikC5UybJ35jbaEnsf8NYrNZta.ZlM5e', 'Beatrice', 'Walton', 'Iphand', '1993-02-23', 'juliesei@ffilledf.com', true, false, true, true);
INSERT INTO account_role(account, role_id)
VALUES(5, 0);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (6,'example.buddie', '$2a$12$ZCu22KPCA81sAnOLB.bZF.s5WHC2ikHTH3ecD0gn/.AJmueqoUazi', 'Courtney', 'Warren', 'Bleah', '1994-03-26', 'grimmly@tlbreaksm.com', true, false, true, true);
INSERT INTO account_role(account, role_id)
VALUES(6, 0);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (7,'HOLSexample', '$2a$12$AlPslWJ///yrhOvyCJQnaOwVYvaOomggiADJsgm6vvUcWqySqxEDm', 'Nettie', 'Flynn', 'Dreigh', '1996-10-30', '420cook@zdecadesgl.com', true, true, false, true);
INSERT INTO account_role(account, role_id)
VALUES(7, 0);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (8, 'calvin.example', '$2a$12$gaY4.bbOeWb8WbzL7Qjuq.UohVLKxG9AQqO9HqA7xTiorfYIenKHy', 'Lydia', 'Torres', 'Ilaford', '1988-03-20', 'mhlangajabulani@gospelyqqv.com', true, true, false, true);
INSERT INTO account_role(account, role_id)
VALUES(8, 0);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (9, 'airesexample', '$2a$12$1NJIlKrzOb1NgFx8OM785OpbaSCyFMd/pMWEmJsJQoFTtEXk2HWte', 'Edie', 'Flowers', 'Oitfield', '1995-05-24', 'mrphish8@skynetengine.xyz', true, true, false, true);
INSERT INTO account_role(account, role_id)
VALUES(9, 0);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (10, 'exampletaffy', '$2a$12$v1uEq2HjAD19/GvCD6OnKepF3mbWNltquhLp6cr51Gi8RiNgNIk7e', 'Yasmine', 'Russell', 'Tipgas', '1985-08-19', 'aphelion000@567map.xyz', true, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(10, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (11, 'examplexena', '$2a$12$DMJAKjqO1Yn0KAuf4YljkunQ4aOhrJGDsgauzzitHsRcx/c1gSsSG', 'Lee', 'Bennett', 'Diccester', '1995-03-01', 'verrrochka@bellatoengineers.com', true, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(11, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (12, 'examplebullet', '$2a$12$ffPXCXNWQT.S5QlYwBrbOusulA8K4N1GNtl/GvSB83EFHJ3Wb1zdK', 'Matilda', 'Collins', 'Ulitset', '1991-07-20', 'bratskaya@ovmail.net', true, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(12, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (13, 'curlyexample5', '$2a$12$plQTBtmJPtWfNm3HPos/6Owq9l/COGPBp6dmJXHWPqkuwlWBtwI06', 'Maiti', 'Dennis', 'Craidale', '1985-10-10', 'cyrus191@faithfulheatingandair.com', false, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(13, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (14, 'whiskersexample', '$2a$12$fy3rVn6iQP3Y./Bx5GlXm.8ChhXgDqi2wvVmQfggtRs7qitlLdn0G', 'Mabel', 'Cooper', 'Luucsall', '1996-11-17', 'jacethereaper@meleni.xyz', false, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(14, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (15, 'starexample', '$2a$12$bW2NAcu6dszn0VnDiMlBdekgsif3p3dhO2WpoKDzx6wUxgtS8AGLu', 'Gabrielle', 'Dunn', 'Xeigh', '2003-05-16', 'ebalaxlinskaya@iron1.xyz', false, true, true, false);
INSERT INTO account_role(account, role_id)
VALUES(15, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (16, 'exampleyin', '$2a$12$MdwrdIyVtumxTW0jjJDGfunbj.tYAf7O/Vt9WJ89HxE6fSStv0LwC', 'Zoya', 'Jordan', 'Jey', '1983-11-20', 'ulus04@steampot.xyz', true, false, false, true);
INSERT INTO account_role(account, role_id)
VALUES(16, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (17, 'exampleCOS', '$2a$12$VoLBlHzRltVVcQLLBaDaY..IFV7jmPmaDt/7.XbCKnT8PTgSJO3F2', 'Kiara', 'Donaldson', 'Xouver', '1993-09-16', 'pking1@finegoldnutrition.com', true, false, false, true);
INSERT INTO account_role(account, role_id)
VALUES(17, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (18, '5exampleyin', '$2a$12$pK6f79.W7ffqXPzCkmR7PO1ISsg0h/etN0pwGIWmD5Ba/DiIDajOu', 'Demi', 'Patton', 'Onipool', '1999-01-17', 'mxzx800ridr@repeatxdu.com', true, false, false, true);
INSERT INTO account_role(account, role_id)
VALUES(18, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (19, 'twinkleexample', '$2a$12$zClkoeaGuYcfrrJPX0hFAueG6l2PCK6uZMMXhJgLjhk2MP11dhFDi', 'Elsie', 'Hall', 'Ansgend', '1990-03-26', 'lover1984@truxamail.com', true, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(19, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (20, 'tuesdayexample', '$2a$12$P/x1QU8vCKg0buhccIB10OWb4eahO2aLis7DFBU2L2dBM2AmY0eOK', 'Paige', 'Oconnor', 'Woimshire', '1983-10-05', 'josejavier79@skorbola.club', true, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(20, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (21, 'whispyexample', '$2a$12$nPYPUTDccQUESktFUEzwzOTOnreSpegEUG1JhtudPpAcHkwV0JNAW', 'Rachel', 'Gibson', 'Sluburgh', '1986-06-27', 'jefoli67@bellatoengineers.com', true, true, true, true);
INSERT INTO account_role(account, role_id)
VALUES(21, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (22, 'examplecubs', '$2a$12$VKBRuWiIf4doUIimellP0OM9Z5wDlODGaRBzKiUOTCPVFk6o0YqpS', 'Louisa', 'Winter', 'Vlukfast', '1986-01-31', 'jeanxze@polycond.eu', false, false, false, false);
INSERT INTO account_role(account, role_id)
VALUES(22, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (23, 'bobbyexample', '$2a$12$0ia8kBecf.ifxV/vHpXG9.XTFtG5BUxSoVqO473N2YlEMjMLZLfzm', 'Loulou', 'Harris', 'Chedpolis', '1987-01-16', 'nidi@astrthelabel.xyz', false, false, false, false);
INSERT INTO account_role(account, role_id)
VALUES(23, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (24, 'Mexample', '$2a$12$50.eV6QxoLKdm2iZfoVtCub.sfIw38X/Bn1btaU2HQfy3b7i6QdUe', 'Eden', 'Porter', 'Glastead', '1993-10-11', 'peterrollow@delaysrnxf.com', false, false, false, false);
INSERT INTO account_role(account, role_id)
VALUES(24, 1);

INSERT INTO account(account_id, username, password, first_name, last_name, homecity, dob, email, is_Account_Non_Expired, is_Account_Non_Locked, is_Credentials_Non_Expired, is_Enabled)
VALUES (25, '5exampletequila', '$2a$12$TGaxeokvn7OrBH1kFXLnYe0lW94fQo9BLn/RKImhQ21BRuFGPFX.K', 'Alicia', 'Lopez', 'Mares', '1985-05-14', 'stariy1@namesloz.com', true, true, false, true);
INSERT INTO account_role(account, role_id)
VALUES(25, 1);
