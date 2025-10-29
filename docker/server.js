console.log('Boot: cargando servidor...');

process.on('unhandledRejection', (err) => {
  console.error('UNHANDLED REJECTION', err);
});
process.on('uncaughtException', (err) => {
  console.error('UNCAUGHT EXCEPTION', err);
});

import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import dotenv from 'dotenv';
import { ping } from './db.js';
import authRoutes from './routes/auth.js';

// ⚠️ si también importas contactRoutes, verifica que el archivo exista.
dotenv.config();

console.log('ENV =>', {
  PORT: process.env.PORT,
  CORS_ORIGIN: process.env.CORS_ORIGIN,
  DATABASE_URL: process.env.DATABASE_URL ? 'ok' : 'missing'
});

const app = express();
app.use(helmet());
app.use(express.json());
app.use(cors({ origin: process.env.CORS_ORIGIN?.split(',') ?? true }));

app.get('/api/health', async (_req, res) => {
  try {
    const ok = await ping();
    res.json({ status: 'ok', db: ok });
  } catch (err) {
    console.error('Error en /api/health', err);
    res.status(500).json({ status: 'error', message: err.message });
  }
});

app.use('/api', authRoutes);

const port = process.env.PORT || 3001;
app.listen(port, () => console.log(`API escuchando en http://localhost:${port}`));
