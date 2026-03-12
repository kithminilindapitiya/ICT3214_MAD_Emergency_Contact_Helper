# 🚨 Emergency Contact Helper

## 📱 Project Overview

**Emergency Contact Helper** is an Android mobile application designed to assist users during emergency situations.
The application allows users to quickly access emergency services, manage personal emergency contacts, and obtain useful emergency information such as nearby hospitals, police services, ambulance services, and first aid instructions.

The goal of this application is to provide **fast and reliable access to emergency support when users need it most**.

This project was developed as part of the **Mobile Application Development (MAD) coursework**.

---

# 👨‍💻 Team Members

| Name                 | Student ID   | Registration No |
| -------------------- | ------------ | --------------- |
| H.M.P.T.K. Herath    | ICT/2022/070 | 5674            |
| L.G.K. Kavindhya     | ICT/2022/071 | 5675            |
| L.G.K.G. Lindapitiya | ICT/2022/072 | 5676            |

---

# ✨ Application Features

### 🚨 Emergency Services

* Quick access to **Ambulance**
* Quick access to **Police**
* Quick access to **Hospitals**
* Emergency SOS trigger functionality

### 📇 Contact Management

* Add personal emergency contacts
* Edit and update contact details
* Save contacts using SQLite database
* Display contacts using RecyclerView

### 👤 User Account System

* User registration
* User login
* Profile management
* Update user details

### ⚡ Quick Actions

* Emergency call button
* First Aid guidance
* Emergency trigger page
* Customizable service contact numbers

---

# 🛠 Technologies Used

| Technology                     | Purpose                   |
| ------------------------------ | ------------------------- |
| **Java**                       | Main programming language |
| **Android Studio**             | Development IDE           |
| **XML**                        | User Interface design     |
| **SQLite**                     | Local database storage    |
| **RecyclerView**               | Display contact lists     |
| **Material Design Components** | UI elements               |
| **Git & GitHub**               | Version control           |

---

# 🗄 Database

The application uses **SQLite database** to store user and emergency contact information.

### Tables Used

**users**

* Stores user account information

**emergency_contacts**

* Stores emergency contacts linked to users

**service_contacts**

* Stores custom emergency service numbers

**contacts**

* Stores additional personal contacts created by the user

---

# 📂 Project Structure

```text
app
 ├── manifests
 │      AndroidManifest.xml
 │
 ├── java
 │   └── com.example.emergencycontacthelper
 │
 │       ├── activities
 │       │      AddContactActivity
 │       │      AmbulanceDetailActivity
 │       │      CheckupDetailActivity
 │       │      Confirmation_Dialog
 │       │      DatabaseHelper
 │       │      Edit_profile
 │       │      EmergencyTriggerActivity
 │       │      FirstAidDetailActivity
 │       │      HomeActivity
 │       │      HospitalDetailActivity
 │       │      LoginActivity
 │       │      ProfileActivity
 │       │      QuickActionsActivity
 │       │      RegisterActivity
 │       │      SOS_contact_update
 │       │      Splash_screen
 │       │      Trigger_page
 │       │      UpdateNumberActivity
 │       │      ViewContactsActivity
 │
 │       ├── fragments
 │       │      ContactsFragment
 │
 │       ├── utils
 │       │      PasswordUtils
 │       │      ContactAdapter
 │       │      EmergencyContact
 │       │      MainActivity
 │
 │       └── models
 │              Contact
 │
 └── res
      ├── drawable
      │      UI icons, shapes and backgrounds
      │
      ├── layout
      │      activity_home.xml
      │      activity_login.xml
      │      activity_register.xml
      │      activity_profile.xml
      │      activity_view_contacts.xml
      │      dialog_add_contact.xml
      │      fragment_contacts.xml
      │      item_contact.xml
      │
      ├── menu
      │      bottom_nav_menu.xml
      │
      ├── values
      │      colors.xml
      │      strings.xml
      │      styles.xml
      │      themes.xml
      │
      └── xml
             backup_rules.xml
             data_extraction_rules.xml
```

---

# ▶ How to Run the Project

### 1️⃣ Clone the repository

```bash
git clone https://github.com/your-repository-link
```

### 2️⃣ Open the project

Open the project using **Android Studio**

### 3️⃣ Sync dependencies

Allow **Gradle** to download required dependencies.

### 4️⃣ Run the application

Run the application on:

* Android Emulator
  or
* Physical Android Device

---

# 📌 Requirements

* Android Studio
* Java JDK
* Android SDK
* Android Emulator or Android Device

---

# 🔮 Future Improvements

* Location-based hospital search
* Google Maps integration
* SMS emergency alert system
* Voice-triggered emergency calls
* Real-time ambulance contact system

---

# 📜 License

This project was developed **for educational purposes only** as part of the Mobile Application Development coursework.
