[![Release](https://img.shields.io/github/v/release/shravankumaruk/serverium-pro)](https://github.com/shravankumaruk/serverium-pro/releases)
[![License](https://img.shields.io/github/license/shravankumaruk/serverium-pro)](https://github.com/shravankumaruk/serverium-pro/blob/main/LICENSE.md)
[![Stars](https://img.shields.io/github/stars/shravankumaruk/serverium-pro?style=social)](https://github.com/shravankumaruk/serverium-pro/stargazers)
[![Forks](https://img.shields.io/github/forks/shravankumaruk/serverium-pro?style=social)](https://github.com/shravankumaruk/serverium-pro/network/members)
[![Issues](https://img.shields.io/github/issues/shravankumaruk/serverium-pro)](https://github.com/shravankumaruk/serverium-pro/issues)
[![Last Commit](https://img.shields.io/github/last-commit/shravankumaruk/serverium-pro)](https://github.com/shravankumaruk/serverium-pro/commits/main)
[![Repo Size](https://img.shields.io/github/repo-size/shravankumaruk/serverium-pro)](https://github.com/shravankumaruk/serverium-pro)
[![Downloads](https://img.shields.io/github/downloads/shravankumaruk/serverium-pro/total)](https://github.com/shravankumaruk/serverium-pro/releases)
[![Java](https://img.shields.io/badge/java-17%2B-blue)](https://www.oracle.com/java/)
[![Tested on JDK](https://img.shields.io/badge/tested_on-Oracle_JDK_24-green)]()
[![Platform](https://img.shields.io/badge/platform-Windows%20|%20Linux-lightgrey)]()
<p align="center">
  <img src="logo.png" alt="Serverium Pro Logo" width="200" />
</p>

# Serverium Pro (SimpleServerApp)

A lightweight Java Swing application that serves static files over HTTP, logs requests, and provides an intuitive GUI for managing your local server.Can be used for testing your web applications easily and manage all requests save and view them for debugging too!

---
[![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white)](https://github.com/shravankumaruk)
[![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)](https://#) 
[![forthebadge](https://forthebadge.com/images/badges/code-it-test-it-break-it.svg)](https://#)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)
[![tested](https://raw.githubusercontent.com/shravanprojects/shravanprojects.github.io/refs/heads/main/serverium-pro/tested-on-oracle-jdk-24.svg)](https://#)



## 🚀 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Installation](#installation)

   * [Windows 11](#windows-11)
   * [Ubuntu / Debian / Kali Linux](#ubuntu--debian--kali-linux)
3. [Usage](#usage)
4. [Screenshots](#screenshots)
5. [Contributing & Star the Project](#contributing--star-the-project)
6. [License](#license)

---

## 📋 Prerequisites

Before running **Serverium Pro**, ensure you have the following installed on your system:

* **Java Development Kit (JDK) 17 or later (tested on Oracle JDK 24)**
* **Git** (to clone the repository)

Verify Java installation:

```bash
java -version
javac -version
```

You should see output like `java version "17"` or higher (If its 24 then great).

---

## 🛠️ Installation

### Windows 11



**🖥️ *Update*:you can download the latest Windows ZIP (with ServeriumPro.exe ready-to-run) from the [Releases](https://github.com/shravankumaruk/serverium-pro/releases) section and simply unzip & launch.**





1. **Install JDK**

   * Download and install the latest JDK from [Oracle](https://www.oracle.com/java/technologies/javase-downloads.html) or [AdoptOpenJDK](https://adoptium.net/).
   * During setup, check **Add to PATH**.

2. **Clone the repository**

   ```powershell
   git clone https://github.com/shravankumaruk/serverium-pro.git
   cd serverium-pro
   ```

3. **Build the project**

   ```powershell
   javac SimpleServerApp.java
   ```

4. **Run the application**

   ```powershell
   java SimpleServerApp
   ```

---

### Ubuntu / Debian / Kali Linux

1. **Install JDK & Git**

   ```bash
   sudo apt update
   sudo apt install -y default-jdk
   ```

2. **Clone the repository**

   ```bash
   git clone https://github.com/shravankumaruk/serverium-pro.git
   cd serverium-pro
   ```

3. **Build the project**

   ```bash
   javac SimpleServerApp.java
   ```

4. **Run the application**

   ```bash
   java SimpleServerApp
   ```

---

## ▶️ Usage

1. **Launch** the GUI by running the application as shown above.
2. **Enter a port** (above 1024).
3. **Select a directory** to serve static files from.
4. click **Start Server**
5. Use **Open Page** to view the served content in your default browser.
6. **Save**, **View**, or **Clear** logs using the respective buttons.
7. **Zoom In/Out** in the log panel or use `Ctrl +` / `Ctrl -`.

---

## 📸 Screenshots

![Start Server & Logs](images/start.png)  
*Figure 1. Starting the server and initial log output.*

![Save Logs Dialog](images/save.png)  
*Figure 2. Saving logs into a `.srep` file.*

![Main Tool Interface](images/tool.png)  
*Figure 3. Main interface showing controls and log panel.*


---


## 🔧Maintenance & Support 

I am committed to promptly updating this project in the event that any vulnerabilities or issues are discovered. Please report any problems or security concerns at: https://github.com/shravankumaruk/serverium-pro/issues 


## ⭐ Contributing & Star the Project

Contributions are welcome! To help improve Serverium Pro:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/YourFeature`)
3. Commit your changes (`git commit -m "Add YourFeature"`)
4. Push to the branch (`git push origin feature/YourFeature`)
5. Open a Pull Request

If you find this tool useful, please ⭐ **star** the repo:

[GitHub • Serverium Pro](https://github.com/shravankumaruk/serverium-pro)

---

## 📝 License

This project is licensed under the [GNU GPL v3.0](LICENSE.md). Feel free to use and modify, provided you retain the **original credit** and license notice.
