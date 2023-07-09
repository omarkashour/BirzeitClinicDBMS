-- Drop the database if it exists
DROP DATABASE IF EXISTS bzuClinic;

-- Create the database
CREATE DATABASE bzuClinic;

-- Switch to the database
USE bzuClinic;

-- Create the physician table
CREATE TABLE physician (
    phys_id INT NOT NULL auto_increment,
    first_name VARCHAR(32),
	last_name VARCHAR(32),
    address VARCHAR(64),
    speciality VARCHAR(32),
    email_address VARCHAR(254),
    phone_number VARCHAR(10),
    gender CHAR,
    PRIMARY KEY (phys_id)
);

-- Create the patient table
CREATE TABLE patient (
    patient_id INT NOT NULL auto_increment,
    first_name VARCHAR(32),
	last_name VARCHAR(32),
    address VARCHAR(64),
    dob DATE,
    email_address VARCHAR(254),
    phone_number VARCHAR(10),
    gender VARCHAR(1),
    weight REAL,
    height REAL,
    PRIMARY KEY (patient_id)
);



-- Create the appointment table
CREATE TABLE appointment (
    ap_id INT NOT NULL auto_increment,
    patient_id INT NOT NULL,
    phys_id INT NOT NULL,
    ap_reason VARCHAR(254),
    ap_date DATE,
    ap_time TIME,
    status VARCHAR(64), -- Complete, Rescheduled, Cancelled
    cost REAL,
    duration TIME,
    PRIMARY KEY (ap_id),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id) ON UPDATE CASCADE,
    FOREIGN KEY (phys_id) REFERENCES physician (phys_id) ON UPDATE CASCADE
);

-- Create the prescription table
CREATE TABLE prescription (
    pr_id INT NOT NULL auto_increment,
    patient_id INT NOT NULL,
    phys_id INT NOT NULL,
    medication_name VARCHAR(64),
    dosage REAL,
    frequency VARCHAR(64),
    PRIMARY KEY (pr_id),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id) ON UPDATE CASCADE,
    FOREIGN KEY (phys_id) REFERENCES physician (phys_id) ON UPDATE CASCADE
);

-- Create the billing_Record table
CREATE TABLE billing_Record (
    record_id INT NOT NULL auto_increment,
    patient_id INT NOT NULL,
    billing_method VARCHAR(48),
    total_amount REAL,
    amount_paid REAL,
    amount_left REAL,
    date_of_billing DATE,
    details VARCHAR(1000),
    payment_status VARCHAR(15),
    PRIMARY KEY (record_id),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id) ON UPDATE CASCADE
);

-- Create the medical_record table
CREATE TABLE medical_record (
    record_id INT NOT NULL auto_increment,
    patient_id INT,
    illness_history VARCHAR(1024) default 'None',
    allergies VARCHAR(1024) default 'None',
    surgeries VARCHAR(1024) default 'None',
    medication_history VARCHAR(3000) default 'None',
    PRIMARY KEY (record_id),
    FOREIGN KEY (patient_id) REFERENCES patient (patient_id) ON UPDATE CASCADE
);

INSERT INTO patient (patient_id, first_name,last_name, address, dob, email_address, phone_number, gender, weight, height)
VALUES
    (1, 'John', 'Doe', '123 Main St', '1990-01-01', 'john.doe@example.com', '1234567890', 'M', 70.5, 180.0),
    (2, 'Jane', 'Smith', '456 Elm St', '1985-05-10', 'jane.smith@example.com', '0987654321', 'F', 65.2, 165.0),
    (3, 'David ', 'ohnson', '789 Oak Ave', '1978-09-15', 'david.johnson@example.com', '5555555555', 'M', 80.0, 175.5),
    (4, 'Sarah', 'Davis', '321 Pine Rd', '1995-03-20', 'sarah.davis@example.com', '9998887776', 'F', 60.8, 160.0),
    (5, 'Michael', 'Wilson', '987 Cedar Ln', '1982-07-25', 'michael.wilson@example.com', '1112223334', 'M', 90.2, 190.0),
    (6, 'Emily', 'Anderson', '654 Walnut Ave', '1992-12-05', 'emily.anderson@example.com', '7776665554', 'F', 55.0, 150.0),
    (7, 'Daniel', 'Martinez', '789 Maple Dr', '1980-04-10', 'daniel.martinez@example.com', '2223334445', 'M', 85.5, 180.5),
    (8, 'Olivia', 'Rodriguez', '321 Birch Rd', '1998-08-15', 'olivia.rodriguez@example.com', '7777777777', 'F', 62.3, 157.5),
    (9, 'Joseph', 'Thompson', '654 Oak St', '1993-02-20', 'joseph.thompson@example.com', '9999999999', 'M', 75.0, 175.0),
    (10, 'Sophia', 'Harris', '987 Pine Ave', '1997-06-25', 'sophia.harris@example.com', '1111111111', 'F', 67.8, 162.0),
    (11, 'Matthew', 'Taylor', '555 Elm St', '1991-01-15', 'matthew.taylor@example.com', '4445556667', 'M', 80.5, 183.0),
    (12, 'Emma', 'Clark', '888 Oak Ave', '1987-05-20', 'emma.clark@example.com', '7778889990', 'F', 58.2, 155.5),
    (13, 'Daniel', 'Baker', '777 Maple Dr', '1979-10-25', 'daniel.baker@example.com', '2224446668', 'M', 87.0, 178.0),
    (14, 'Olivia', 'Walker', '333 Birch Rd', '1996-04-05', 'olivia.walker@example.com', '8889990001', 'F', 63.8, 160.0),
    (15, 'Andrew', 'Wright', '666 Oak St', '1990-09-10', 'andrew.wright@example.com', '3334445556', 'M', 78.0, 180.5),
    (16, 'Grace','Allen', '555 Pine Ave', '1994-01-25', 'grace.allen@example.com', '6667778889', 'F', 64.5, 157.0),
    (17, 'Benjamin ','Lewis', '444 Walnut St', '1988-06-01', 'benjamin.lewis@example.com', '5556667778', 'M', 83.2, 185.5),
    (18, 'Ava', 'King', '222 Cedar Ave', '1993-11-06', 'ava.king@example.com', '2223334445', 'F', 59.0, 150.0),
    (19, 'William ', 'Green', '777 Maple Ln', '1986-02-11', 'william.green@example.com', '9990001112', 'M', 92.5, 195.0),
    (20, 'Mia', 'Hall', '999 Oak Dr', '1999-07-16', 'mia.hall@example.com', '4445556667', 'F', 66.2, 163.5);
    
    
    INSERT INTO physician (phys_id, first_name,last_name, address, speciality, email_address, phone_number, gender)
VALUES
    (1, 'Dr. John', 'Smith', '123 Main St', 'Cardiology', 'john.smith@example.com', '1234567890', 'M'),
    (2, 'Dr. Emily', 'Johnson', '456 Elm St', 'Pediatrics', 'emily.johnson@example.com', '0987654321', 'F'),
    (3, 'Dr. Michael', 'Brown', '789 Oak Ave', 'Orthopedics', 'michael.brown@example.com', '5555555555', 'M'),
    (4, 'Dr. Sarah', 'Davis', '321 Pine Rd', 'Dermatology', 'sarah.davis@example.com', '9998887776', 'F'),
    (5, 'Dr. Jennifer', 'Wilson', '987 Cedar Ln', 'Ophthalmology', 'jennifer.wilson@example.com', '1112223334', 'F'),
    (6, 'Dr. Robert', 'Anderson', '654 Walnut Ave', 'Neurology', 'robert.anderson@example.com', '7776665554', 'M'),
    (7, 'Dr. Jessica', 'Martinez', '789 Maple Dr', 'Gastroenterology', 'jessica.martinez@example.com', '2223334445', 'F'),
    (8, 'Dr. David', 'Thompson', '321 Birch Rd', 'Endocrinology', 'david.thompson@example.com', '7777777777', 'M'),
    (9, 'Dr. Samantha', 'Harris', '654 Oak St', 'Oncology', 'samantha.harris@example.com', '9999999999', 'F'),
    (10, 'Dr. Christopher', 'Wilson', '987 Pine Ave', 'Psychiatry', 'christopher.wilson@example.com', '1111111111', 'M'),
    (11, 'Dr. Elizabeth', 'Taylor', '555 Elm St', 'Obstetrics and Gynecology', 'elizabeth.taylor@example.com', '4445556667', 'F'),
    (12, 'Dr. Benjamin', 'Clark', '888 Oak Ave', 'Urology', 'benjamin.clark@example.com', '7778889990', 'M'),
    (13, 'Dr. Olivia', 'Baker', '777 Maple Dr', 'Radiology', 'olivia.baker@example.com', '2224446668', 'F'),
    (14, 'Dr. Daniel', 'Walker', '333 Birch Rd', 'Allergy and Immunology', 'daniel.walker@example.com', '8889990001', 'M'),
    (15, 'Dr. Grace', 'Wright', '666 Oak St', 'Pulmonology', 'grace.wright@example.com', '3334445556', 'F'),
    (16, 'Dr. Matthew', 'Allen', '555 Pine Ave', 'Otolaryngology', 'matthew.allen@example.com', '6667778889', 'M'),
    (17, 'Dr. Ava', 'Lewis', '444 Walnut St', 'Family Medicine', 'ava.lewis@example.com', '5556667778', 'F'),
    (18, 'Dr. William', 'King', '222 Cedar Ave', 'Internal Medicine', 'william.king@example.com', '2223334445', 'M'),
    (19, 'Dr. Emma', 'Green', '777 Maple Ln', 'Rheumatology', 'emma.green@example.com', '9990001112', 'F'),
    (20, 'Dr. Noah', 'Hall', '999 Oak Dr', 'Anesthesiology', 'noah.hall@example.com', '4445556667', 'M');
    
    
  INSERT INTO appointment (ap_id, patient_id, phys_id, ap_reason, ap_date, ap_time, status, cost, duration)
VALUES
    (1, 1, 1, 'Regular check-up', '2023-06-27', '09:00:00', 'Confirmed', 50.0, '00:30:00'),
    (2, 2, 2, 'Pediatric consultation', '2023-07-05', '10:30:00', 'Confirmed', 60.0, '00:45:00'),
    (3, 3, 3, 'Orthopedic consultation', '2023-07-13', '14:15:00', 'Confirmed', 75.0, '00:45:00'),
    (4, 4, 4, 'Skin allergy examination', '2023-07-19', '11:45:00', 'Confirmed', 80.0, '01:00:00'),
    (5, 5, 5, 'Eye examination', '2023-07-26', '08:30:00', 'Confirmed', 55.0, '00:30:00'),
    (6, 6, 6, 'Neurological evaluation', '2023-07-01', '15:30:00', 'Confirmed', 90.0, '01:00:00'),
    (7, 7, 7, 'Gastrointestinal consultation', '2023-08-09', '12:30:00', 'Confirmed', 70.0, '00:45:00'),
    (8, 8, 8, 'Diabetes management', '2023-08-16', '09:15:00', 'Confirmed', 60.0, '00:45:00'),
    (9, 9, 9, 'Oncology follow-up', '2023-08-23', '16:00:00', 'Confirmed', 85.0, '01:00:00'),
    (10, 10, 10, 'Psychological counseling', '2023-08-31', '13:00:00', 'Confirmed', 65.0, '00:45:00'),
    (11, 11, 11, 'Prenatal check-up', '2023-09-06', '10:00:00', 'Confirmed', 70.0, '00:45:00'),
    (12, 12, 12, 'Urological consultation', '2023-09-14', '14:45:00', 'Confirmed', 80.0, '01:00:00'),
    (13, 13, 13, 'Radiological imaging', '2023-09-21', '11:15:00', 'Confirmed', 95.0, '01:15:00'),
    (14, 14, 14, 'Allergy testing', '2023-09-28', '16:30:00', 'Confirmed', 50.0, '00:30:00'),
    (15, 15, 15, 'Pulmonary function test', '2023-10-04', '13:45:00', 'Confirmed', 75.0, '00:45:00'),
    (16, 16, 16, 'Ear, nose, and throat examination', '2023-10-12', '10:45:00', 'Confirmed', 65.0, '00:45:00'),
    (17, 17, 17, 'Family medicine consultation', '2023-10-19', '15:15:00', 'Confirmed', 80.0, '01:00:00'),
    (18, 18, 18, 'Internal medicine check-up', '2023-10-26', '12:15:00', 'Confirmed', 60.0, '00:45:00'),
    (19, 19, 19, 'Rheumatology evaluation', '2023-11-01', '09:45:00', 'Confirmed', 70.0, '00:45:00'),
    (20, 20, 20, 'Anesthesia consultation', '2023-11-08', '14:00:00', 'Confirmed', 80.0, '01:00:00');

    
    INSERT INTO prescription (pr_id, patient_id, phys_id, medication_name, dosage, frequency)
VALUES
    (1, 1, 1, 'Medicine A', 2.5, 'Once daily'),
    (2, 2, 2, 'Medicine B', 1.0, 'Twice daily'),
    (3, 3, 3, 'Medicine C', 0.5, 'Three times daily'),
    (4, 4, 4, 'Medicine D', 1.5, 'Once daily'),
    (5, 5, 5, 'Medicine E', 2.0, 'Once daily'),
    (6, 6, 6, 'Medicine F', 1.0, 'Twice daily'),
    (7, 7, 7, 'Medicine G', 0.5, 'Three times daily'),
    (8, 8, 8, 'Medicine H', 1.5, 'Once daily'),
    (9, 9, 9, 'Medicine I', 2.0, 'Once daily'),
    (10, 10, 10, 'Medicine J', 1.0, 'Twice daily'),
    (11, 11, 11, 'Medicine K', 0.5, 'Three times daily'),
    (12, 12, 12, 'Medicine L', 1.5, 'Once daily'),
    (13, 13, 13, 'Medicine M', 2.0, 'Once daily'),
    (14, 14, 14, 'Medicine N', 1.0, 'Twice daily'),
    (15, 15, 15, 'Medicine O', 0.5, 'Three times daily'),
    (16, 16, 16, 'Medicine P', 1.5, 'Once daily'),
    (17, 17, 17, 'Medicine Q', 2.0, 'Once daily'),
    (18, 18, 18, 'Medicine R', 1.0, 'Twice daily'),
    (19, 19, 19, 'Medicine S', 0.5, 'Three times daily'),
    (20, 20, 20, 'Medicine T', 1.5, 'Once daily');    

INSERT INTO billing_Record (record_id, patient_id, billing_method, total_amount, amount_paid, amount_left, date_of_billing, details, payment_status)
VALUES
(1, 1, 'Credit Card', 150.0, 100.0, 50.0, '2023-05-17', 'Annual check-up', 'Unpaid'),
(2, 2, 'Cash', 80.0, 80.0, 0.0, '2022-11-30', 'Skin rash treatment', 'Paid'),
(3, 3, 'Insurance', 200.0, 0.0, 200.0, '2023-03-21', 'Child vaccination', 'Unpaid'),
(4, 4, 'Credit Card', 120.0, 120.0, 0.0, '2023-06-01', 'Flu shot', 'Paid'),
(5, 5, 'Cash', 50.0, 0.0, 50.0, '2022-08-07', 'Allergy testing', 'Unpaid'),
(6, 6, 'Insurance', 300.0, 150.0, 150.0, '2023-01-14', 'Physical therapy', 'Unpaid'),
(7, 7, 'Credit Card', 180.0, 100.0, 80.0, '2023-04-30', 'Dental cleaning', 'Unpaid'),
(8, 8, 'Cash', 90.0, 90.0, 0.0, '2022-07-18', 'Eye exam', 'Paid'),
(9, 9, 'Insurance', 250.0, 0.0, 250.0, '2023-02-12', 'MRI scan', 'Unpaid'),
(10, 10, 'Credit Card', 200.0, 200.0, 0.0, '2022-09-23', 'Colonoscopy', 'Paid'),
(11, 11, 'Cash', 70.0, 0.0, 70.0, '2023-06-13', 'Blood test', 'Unpaid'),
(12, 12, 'Insurance', 150.0, 100.0, 50.0, '2022-10-09', 'X-ray', 'Unpaid'),
(13, 13, 'Credit Card', 100.0, 100.0, 0.0, '2022-12-29', 'Dermatology consultation', 'Paid'),
(14, 14, 'Cash', 120.0, 120.0, 0.0, '2023-01-20', 'Physical examination', 'Paid'),
(15, 15, 'Insurance', 180.0, 150.0, 30.0, '2023-03-08', 'Pulmonary function test', 'Unpaid'),
(16, 16, 'Credit Card', 80.0, 0.0, 80.0, '2022-11-12', 'Medication refill', 'Unpaid'),
(17, 17, 'Cash', 200.0, 200.0, 0.0, '2022-08-29', 'Gynecological exam', 'Paid'),
(18, 18, 'Insurance', 90.0, 90.0, 0.0, '2023-05-03', 'Immunization', 'Paid'),
(19, 19, 'Credit Card', 140.0, 140.0, 0.0, '2022-10-28', 'Nutritional counseling', 'Paid'),
(20, 20, 'Cash', 160.0, 0.0, 160.0, '2023-02-07', 'Physical rehabilitation', 'Unpaid');
    
    INSERT INTO medical_record (record_id, patient_id, illness_history, allergies, surgeries, medication_history)
VALUES
    (1, 1, 'History of allergies and asthma', 'Pollen, dust', 'Appendectomy, Tonsillectomy', 'Medicine A, Medicine B'),
    (2, 2, 'No significant illness history', 'None', 'Appendectomy', 'Medicine C'),
    (3, 3, 'Hypertension, diabetes', 'Peanuts, shellfish', 'None', 'Medicine D, Medicine E'),
    (4, 4, 'Eczema, seasonal allergies', 'Dust mites', 'None', 'Medicine F'),
    (5, 5, 'Myopia, astigmatism', 'None', 'None', 'Medicine G'),
    (6, 6, 'Migraine headaches', 'None', 'None', 'Medicine H'),
    (7, 7, 'Gastric ulcers', 'None', 'Gallbladder removal', 'Medicine I, Medicine J'),
    (8, 8, 'Thyroid disorder', 'None', 'None', 'Medicine K'),
    (9, 9, 'Breast cancer survivor', 'None', 'Mastectomy', 'Medicine L'),
    (10, 10, 'Anxiety disorder', 'None', 'None', 'Medicine M, Medicine N'),
    (11, 11, 'Pregnancy', 'None', 'None', 'Prenatal vitamins'),
    (12, 12, 'Kidney stones', 'None', 'Kidney stone removal', 'Medicine O'),
    (13, 13, 'No significant illness history', 'None', 'None', 'None'),
    (14, 14, 'Allergic rhinitis', 'Pollen, pet dander', 'None', 'Medicine P'),
    (15, 15, 'Asthma', 'Dust, mold', 'None', 'Medicine Q'),
    (16, 16, 'Sinusitis', 'None', 'Sinus surgery', 'Medicine R'),
    (17, 17, 'Hypertension', 'None', 'None', 'Medicine S'),
    (18, 18, 'No significant illness history', 'None', 'None', 'None'),
    (19, 19, 'Rheumatoid arthritis', 'None', 'Knee replacement', 'Medicine T, Medicine U'),
    (20, 20, 'No significant illness history', 'None', 'None', 'None');
