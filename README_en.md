# Survey System

A full-stack survey system with mobile-friendly questionnaire filling and PC admin dashboard.

## Features

- **Mobile Questionnaire Page**: Optimized for mobile users with touch-friendly interface
- **PC Admin Dashboard**: Feature-rich management interface using Element Plus UI
- **Question Types**: Single choice, multiple choice, dropdown, text input
- **Image Support**: Display images in questions
- **Dual Mode**: Demo mode with static JSON data or API mode with backend
- **Statistics**: Real-time response statistics and data export

## Tech Stack

**Frontend**: Vue 3, Tailwind CSS, Element Plus, Pinia, Vue Router

**Backend**: Spring Boot 3.x, MyBatis-Plus, Spring Security + JWT, MySQL, Redis

## Quick Start

### Frontend (Demo Mode)

```bash
cd survey-system
npm install
npm run dev
```

### Backend (Full Stack)

**Prerequisites**: JDK 17+, MySQL 8.0+, Redis (optional)

**Setup Database**
```bash
mysql -u root -p < survey-backend/src/main/resources/schema.sql
mysql -u root -p < survey-backend/src/main/resources/data.sql
```

**Start Backend**
```bash
cd survey-backend
mvn spring-boot:run
```

**Switch to API Mode**
Edit `survey-system/src/config.js`:
```javascript
export const DEMO_MODE = false
export const API_BASE_URL = 'http://localhost:8080/api'
```

## Project Structure

```
├── survey-system/          # Frontend (Vue 3)
│   ├── src/
│   │   ├── api/           # API service layer
│   │   ├── components/    # Vue components
│   │   ├── views/         # Page views
│   │   ├── stores/        # Pinia stores
│   │   └── config.js       # Configuration
│
└── survey-backend/          # Backend (Spring Boot)
    └── src/main/java/com/survey/
        ├── controller/      # REST controllers
        ├── service/         # Business logic
        ├── mapper/          # MyBatis mappers
        └── entity/          # Database entities
```

## API Documentation

Visit: http://localhost:8080/api/doc.html

## Default Account

- Username: `admin`
- Password: `admin123`

## License

MIT
