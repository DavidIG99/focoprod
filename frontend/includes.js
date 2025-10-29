// includes.js (normalize footer) v6
document.addEventListener('DOMContentLoaded', async () => {
  const slots = Array.from(document.querySelectorAll('[data-include]'));

  // 1) Cargar includes en paralelo
  if (slots.length) {
    await Promise.all(slots.map(async (el) => {
      let url = el.getAttribute('data-include') || '';
      if (!url) return;
      if (!/\?v=/.test(url)) url += (url.includes('?') ? '&' : '?') + 'v=6';
      try {
        const res = await fetch(url, { cache: 'no-cache' });
        el.innerHTML = res.ok ? (await res.text()) : `Error al cargar ${url}`;
      } catch {
        el.innerHTML = `No se pudo cargar ${url}. ¿file:// ? Usa Live Server.`;
      }
    }));
  }

  // 2) Normalizar footer: año + 3er botón X si faltan
  const normalizeFooter = () => {
    const footer = document.querySelector('.footer-brand');
    if (!footer) return;

    // 2a) Año
    const yearSpan = footer.querySelector('#year') || (() => {
      // si no existe, lo creamos dentro de .footer-copy
      let copy = footer.querySelector('.footer-copy');
      if (!copy) {
        copy = document.createElement('div');
        copy.className = 'footer-copy';
        footer.querySelector('.footer-inner')?.appendChild(copy);
      }
      const span = document.createElement('span');
      span.id = 'year';
      copy.innerHTML = `© <span id="year"></span> ProdMind • Todos los derechos reservados`;
      return copy.querySelector('#year');
    })();
    if (yearSpan && !yearSpan.textContent) {
      yearSpan.textContent = new Date().getFullYear();
    }

    // 2b) Botón X (si solo hay 2 enlaces)
    const social = footer.querySelector('.footer-social');
    if (social) {
      const links = social.querySelectorAll('a');
      const hasX = [...links].some(a => a.dataset.social === 'x');
      if (links.length < 3 || !hasX) {
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

  // Normaliza ahora y por si el include llegó tarde
  normalizeFooter();
  const mo = new MutationObserver(normalizeFooter);
  mo.observe(document.body, { childList: true, subtree: true });
  setTimeout(() => mo.disconnect(), 3000);

  // 3) Click seguro para X (sin palabra “twitter” en HTML)
  document.addEventListener('click', (e) => {
    const x = e.target.closest('a[data-social="x"]');
    if (!x) return;
    e.preventDefault();
    const url = x.getAttribute('data-href') || 'https://x.com';
    window.open(url, '_blank', 'noopener,noreferrer');
  });

  // 4) Mini debug sin consola (Shift+D)
  const showDebug = () => {
    const box = document.getElementById('pm-debug') || document.createElement('div');
    box.id = 'pm-debug';
    box.style.cssText = 'position:fixed;left:16px;bottom:16px;z-index:99999;background:#222;color:#fff;padding:10px 12px;border-radius:10px;font:12px/1.4 system-ui;box-shadow:0 6px 18px rgba(0,0,0,.4);max-width:70vw';
    const links = document.querySelectorAll('.footer-social a').length;
    const yearText = (document.getElementById('year')||{}).textContent || '(vacío)';
    const details = [...document.querySelectorAll('.footer-social a')].map((a,i)=> (
      `${i+1}) data-social="${a.dataset.social||''}" href="${a.getAttribute('href')}" data-href="${a.getAttribute('data-href')||''}"`
    )).join('<br>');
    box.innerHTML = `<b>DEBUG ProdMind</b> · <a href="#" id="pm-close" style="color:#0bf">cerrar</a><br>
      Botones: <b>${links}</b> &nbsp;|&nbsp; Año: <b>${yearText}</b><br>${details}`;
    if (!box.parentNode) document.body.appendChild(box);
    document.getElementById('pm-close').onclick = (ev)=>{ev.preventDefault(); box.remove();}
  };
  window.addEventListener('keydown', (e) => { if (e.shiftKey && (e.key==='D'||e.key==='d')) showDebug(); });
});
// Marca el enlace activo en el footer (aria-current)
const here = location.pathname.split('/').pop() || 'index.html';
document.querySelectorAll('.footer-nav a').forEach(a => {
  const href = a.getAttribute('href') || '';
  if (href.endsWith(here)) a.setAttribute('aria-current', 'page');
});
