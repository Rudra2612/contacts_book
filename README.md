
# Contact Book App 📒  
An offline Contact Book app built entirely using **Jetpack Compose**, offering a seamless experience for adding, editing, managing, and sharing contacts. It features a modern UI, dynamic form inputs, image support, and multi-contact operations – all stored locally using **SQLite**.

---

## ✨ Highlights

1. **Home Screen**
   - Displays all saved contacts with alphabetical sorting.
   - Dynamic search functionality (by name or number).
   - Long-press to activate multi-selection mode for sharing or deleting.
   - Floating Action Button (FAB) to add a new contact.

2. **Add/Edit Contact**
   - Add multiple:
     - 📞 Phone numbers
     - ✉️ Emails
     - 📍 Addresses
   - Assign labels (Mobile, Home, Work, Custom).
   - Add Birthday field with a Date Picker.
   - Optionally add/remove a profile image.
   - Real-time field addition/removal.

3. **Contact Details Screen**
   - Shows complete contact info.
   - Options to:
     - ⭐ Mark as favorite
     - ✏️ Edit contact
     - 🗑️ Delete contact
     - 📤 Share contact info
     - 👁️ View full-size profile image

4. **Image Viewer**
   - Displays full-size image in a clean, centered layout.

5. **Splash Screen**
   - Simple branded splash that transitions to the home screen on launch.

---

## 🛠 Built With
- **Jetpack Compose** for UI
- **Kotlin**
- **SQLite** (via `SQLiteOpenHelper`) for local database
- **Glide** for image loading and caching

---

## 📁 Folder Structure (Main Screens)
- `Home.kt` → Displays all contacts
- `AddOrEditContact.kt` → Dynamic form UI for contact input
- `ContactDetails.kt` → Detailed contact view and options
- `ImageDisplay.kt` → Full screen image view
- `Splash.kt` → Launch animation and redirection

---

## 🧠 Features to Expand
- Import/Export contacts as `.vcf`
- Sync with Google Contacts
- Backup/Restore feature

---

## 📷 Screenshots
_Add screenshots of home screen, add contact, and contact details here_

---

## 📦 How to Run
1. Clone the repository  
2. Open in **Android Studio**  
3. Run on emulator or physical device (API 21+ recommended)
