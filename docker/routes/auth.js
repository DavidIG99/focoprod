import { Router } from 'express';
import bcrypt from 'bcrypt'; 
import { pool } from '../db.js';

const router = Router();
const ROUNDS = Number(process.env.BCRYPT_ROUNDS ?? 12);


router.post('/register', async (req, res) => {
  try {
    const { nombre, cedula, email, celular, password } = req.body;
    if (!nombre || !cedula || !email || !celular || !password)
      return res.status(400).json({ error: 'Todos los campos son requeridos' });
    if (String(password).length < 8)
      return res.status(400).json({ error: 'La contraseña debe tener al menos 8 caracteres' });

    const hash = await bcrypt.hash(password, ROUNDS);
    const q = `
      INSERT INTO users (nombre, cedula, email, celular, password_hash)
      VALUES ($1, $2, $3, $4, $5)
      RETURNING id, nombre, cedula, email, celular, created_at
    `;
    const { rows } = await pool.query(q, [nombre, cedula, email, celular, hash]);
    res.status(201).json({ user: rows[0] });
  } catch (err) {
    if (err.code === '23505') {
      const field = (err.detail || '').includes('(cedula)') ? 'cédula' : 'email';
      return res.status(409).json({ error: `La ${field} ya está registrada` });
    }
    console.error(err);
    res.status(500).json({ error: 'Error al registrar usuario' });
  }
});

export default router;
