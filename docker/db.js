import pkg from 'pg';
import dotenv from 'dotenv';
dotenv.config();
const { Pool } = pkg;
export const pool = new Pool({ connectionString: process.env.DATABASE_URL });
export async function ping() {
  const { rows } = await pool.query('SELECT 1 as ok');
  return rows[0].ok === 1;
}
