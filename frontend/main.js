document.addEventListener('DOMContentLoaded', async () => {
  const slots = Array.from(document.querySelectorAll('[data-include]'));

  if (slots.length) {
    await Promise.all(slots.map(async (el) => {
      let url = el.getAttribute('data-include') || '';
      if (!url) return;
      if (!/\?v=/.test(url)) url += (url.includes('?') ? '&' : '?') + 'v=7';
      try {
        const res = await fetch(url, { cache: 'no-cache' });
        el.innerHTML = res.ok ? (await res.text()) : '';
      } catch {
      }
    }));
  }

  const normalizeFooter = () => {
    const footer = document.querySelector('.footer-brand');
    if (!footer) return;

    const span = footer.querySelector('#year');
    if (span && !span.textContent) span.textContent = new Date().getFullYear();

    const social = footer.querySelector('.footer-social');
    if (social) {
      const hasX = !!social.querySelector('a[data-social="x"]');
      if (!hasX) {
        const a = document.createElement('a');
        a.setAttribute('data-social', 'x');
        a.setAttribute('href', '#');
        a.setAttribute('data-href', 'https://x.com/tucuenta');
        a.setAttribute('target', '_blank');
        a.setAttribute('rel', 'noopener');
        a.setAttribute('aria-label', 'X');
        a.innerHTML = `
          <svg viewBox="0 0 24 24" width="26" height="26" aria-hidden="true">
            <path fill="currentColor"
              d="M18.2 2H21l-6.3 7.2L22 22h-6.9l-4.4-6-5 6H2l6.8-7.8L2 2h7l4 5.4L18.2 2Zm-1.2 18.2h1.8L7.2 3.7H5.4l11.6 16.5Z"/>
          </svg>`;
        social.appendChild(a);
      }
    }
  };

  normalizeFooter();
  const mo = new MutationObserver((m) => {
    normalizeFooter();
    // desconectar cuando ya exista el año
    const y = document.getElementById('year');
    if (y && y.textContent) mo.disconnect();
  });
  mo.observe(document.body, { childList: true, subtree: true });

  document.addEventListener('click', (e) => {
    const x = e.target.closest('a[data-social="x"]');
    if (!x) return;
    e.preventDefault();
    const url = x.getAttribute('data-href') || 'https://x.com';
    window.open(url, '_blank', 'noopener,noreferrer');
  });
});
// main.js
(() => {
  const API = 'http://localhost:3001/api'; // <- ajusta host/puerto si es necesario

  // Utilidad de toast (usa la tuya si existe)
  const toast = (msg, ms = 2500) => {
    if (window.toast) return window.toast(msg, ms);
    alert(msg);
  };

  // Añade/quita estado de "loading" en botones
  function setLoading($btn, on, textWhileLoading = 'Enviando…') {
    if (!$btn) return;
    if (on) {
      $btn.dataset._orig = $btn.textContent;
      $btn.disabled = true;
      $btn.classList.add('btn--loading');
      $btn.textContent = textWhileLoading;
    } else {
      $btn.disabled = false;
      $btn.classList.remove('btn--loading');
      if ($btn.dataset._orig) $btn.textContent = $btn.dataset._orig;
    }
  }

  // ====== REGISTRO ======
  function attachRegistro() {
    const $form = document.querySelector('#form-registro');
    if (!$form) return;

    const $btn = $form.querySelector('button[type="submit"]');

    $form.addEventListener('submit', async (e) => {
      e.preventDefault();

      // Validación HTML5
      if (!$form.checkValidity()) {
        // dispara validación nativa si algo falta
        $form.reportValidity?.();
        return;
      }

      // Toma valores
      const fd = new FormData($form);
      const p = Object.fromEntries(fd.entries());

      // Limpieza básica
      p.nombre  = (p.nombre  || '').trim();
      p.cedula  = (p.cedula  || '').trim();
      p.email   = (p.email   || '').trim();
      p.celular = (p.celular || '').trim();

      setLoading($btn, true);

      try {
        const res = await fetch(`${API}/register`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            nombre:  p.nombre,
            cedula:  p.cedula,
            email:   p.email,
            celular: p.celular,
            password: p.password
          })
        });

        const data = await res.json().catch(() => ({}));
        if (!res.ok) {
          // mensajes comunes desde el backend
          throw new Error(data.error || 'No se pudo registrar. Intenta de nuevo.');
        }

        toast('¡Registro exitoso!');
        $form.reset();
      } catch (err) {
        toast(err.message || 'Ups, algo salió mal.');
      } finally {
        setLoading($btn, false);
      }
    });
  }

  // ====== CONTACTO (opcional) ======
  function attachContacto() {
    const $form = document.querySelector('#form-contacto');
    if (!$form) return;

    const $btn = $form.querySelector('button[type="submit"]');

    $form.addEventListener('submit', async (e) => {
      e.preventDefault();
      if (!$form.checkValidity()) {
        $form.reportValidity?.();
        return;
      }

      const fd = new FormData($form);
      const p = Object.fromEntries(fd.entries());

      setLoading($btn, true);
      try {
        const res = await fetch(`${API}/contact`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({
            name: p.nombre || p.name,
            email: p.email,
            message: p.mensaje || p.message
          })
        });
        const data = await res.json().catch(() => ({}));
        if (!res.ok) throw new Error(data.error || 'No se pudo enviar el mensaje.');
        toast('¡Mensaje enviado!');
        $form.reset();
      } catch (err) {
        toast(err.message || 'Ups, algo salió mal.');
      } finally {
        setLoading($btn, false);
      }
    });
  }

  // Ejecutar cuando el DOM esté listo (y tus includes ya cargados)
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
      attachRegistro();
      attachContacto();
    });
  } else {
    attachRegistro();
    attachContacto();
  }
})();
