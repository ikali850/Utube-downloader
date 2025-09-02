# 🎬 Utube Video & Audio Downloader

> ⚠️ **Work in Progress**: This project is currently under active development. Some features may be incomplete or subject to change.

## 📌 Project Description

A **YouTube Video & Audio Downloader** built with **Spring Boot**, **Java**, and **Thymeleaf**, using **yt-dlp** under the hood for video/audio processing. This web application allows users to easily download YouTube videos or extract audio in various formats and qualities through a clean, responsive interface.

## ✨ Features

- 🎥 **Video Downloads**: Download YouTube videos in multiple formats (MP4, WebM, etc.)
- 🎵 **Audio Extraction**: Extract and download audio only (MP3, M4A, etc.)
- ⚡ **Fast Processing**: Efficient downloads powered by **yt-dlp**
- 🌐 **Web Interface**: Simple and clean UI built with Thymeleaf and Bootstrap
- 📂 **Smart File Management**: Automatic file naming and output path management
- 🖼️ **Rich Previews**: Display video thumbnails, titles, and metadata
- 📱 **Responsive Design**: Works seamlessly on desktop and mobile devices

## 🛠 Tech Stack

- **Backend**: Java 17+, Spring Boot 3.x
- **Frontend**: Thymeleaf, Bootstrap, HTML5/CSS3
- **Build Tool**: Maven
- **Video Processing**: yt-dlp
- **Templating**: Thymeleaf

## ⚙️ Installation

Follow these steps to set up the project locally:

### Prerequisites
- **Java 17+** installed
- **Maven 3.6+** installed
- **yt-dlp** installed and accessible in system PATH

### Setup Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/ikali850/Utube-downloader.git
   cd Utube-downloader
   ```

2. **Install yt-dlp**
   ```bash
   # Using pip (recommended)
   pip install yt-dlp
   
   # Or using package manager
   # Ubuntu/Debian
   sudo apt install yt-dlp
   
   # macOS with Homebrew
   brew install yt-dlp
   ```
   
   Alternatively, download the binary from [yt-dlp releases](https://github.com/yt-dlp/yt-dlp/releases).

3. **Build the project**
   ```bash
   mvn clean install
   ```

4. **Configure application (optional)**
   Update `src/main/resources/application.properties` to customize:
   - Download directory path
   - Server port
   - Other application settings

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

6. **Access the application**
   Open your browser and navigate to: **http://localhost:8080**

## 🎯 Usage Guide

1. **Launch the Application**  
   Navigate to `http://localhost:8080` in your web browser

2. **Enter YouTube URL**  
   Paste any valid YouTube video link into the input field

3. **Choose Download Type**
   - **Video (with audio)**: Select your preferred video format (MP4 recommended)
   - **Audio only**: Choose audio format (MP3, M4A, etc.)

4. **Select Quality**  
   Pick from available quality options (720p, 1080p, etc.)

5. **Download**  
   Click the download button and wait for processing

6. **Access Files**  
   Downloaded files will be saved in your configured downloads directory

💡 **Pro Tip**: MP4 format with 720p quality offers the best balance between file size and quality for most use cases.

## 🚀 Development Status

This project is currently **under construction** with ongoing improvements:

### ✅ Completed Features
- Basic YouTube video downloading
- Web interface with Thymeleaf
- yt-dlp integration
- Spring Boot backend structure

### 🔄 In Progress
- Audio extraction functionality
- Quality selection options
- Download progress tracking
- Error handling improvements

### 📋 Planned Features
- [ ] Playlist download support
- [ ] Batch download functionality
- [ ] Download history
- [ ] User preferences
- [ ] API endpoints
- [ ] Docker containerization

## 🤝 Contributing

Contributions are welcome! Since this project is under active development:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## ⚠️ Disclaimer

This tool is for educational and personal use only. Please respect YouTube's Terms of Service and copyright laws. Users are responsible for ensuring they have the right to download and use any content.

## 🐛 Issues & Support

If you encounter any issues or have suggestions:
- Open an issue on [GitHub Issues](https://github.com/ikali850/Utube-downloader/issues)
- Provide detailed information about the problem
- Include system information and error logs if applicable

## 👨‍💻 Author

**Arvind**  
- GitHub: [@ikali850](https://github.com/ikali850)
- Instagram: [@themannu850](https://www.instagram.com/themannu850/)

---

⭐ **Star this repository** if you find it useful!

> **Note**: This project is actively being developed. Check back for updates and new features!
