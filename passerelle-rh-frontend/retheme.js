const fs = require('fs');
const path = require('path');

function walkDir(dir, callback) {
    fs.readdirSync(dir).forEach(f => {
        let dirPath = path.join(dir, f);
        let isDirectory = fs.statSync(dirPath).isDirectory();
        if (isDirectory) {
            walkDir(dirPath, callback);
        } else if (dirPath.endsWith('.html')) {
            callback(path.join(dir, f));
        }
    });
}

walkDir('src/app/components', function (filePath) {
    let content = fs.readFileSync(filePath, 'utf8');
    content = content.replace(/emerald/g, 'blue');
    content = content.replace(/teal/g, 'cyan');
    content = content.replace(/amber/g, 'orange');
    content = content.replace(/bg-gradient-to-r from-slate-800 to-slate-900/g, 'bg-white');
    content = content.replace(/bg-gradient-to-r from-slate-800 via-slate-900 to-slate-800/g, 'bg-blue-900');
    content = content.replace(/nav class="bg-white/g, 'nav class="bg-white/90 backdrop-blur-md');
    // For admin dashboards with dark navs initially:
    content = content.replace(/text-slate-300 hover:text-white/g, 'text-slate-600 hover:text-blue-600');
    content = content.replace(/text-white font-black text-lg/g, 'text-white font-bold text-xl');
    content = content.replace(/border-slate-700/g, 'border-slate-200');

    if (filePath.includes('admin-')) {
        content = content.replace(/text-white">PASSERELLE/g, 'text-slate-900">PASSERELLE');
    }

    fs.writeFileSync(filePath, content, 'utf8');
});

// Also check public-profile and passeport, which might have "PASSERELLE <span class="text-blue-600">RH</span>" on dark bg if not addressed.
// Actually, public profile and passeport use the same header style:
walkDir('src/app/components/public-profile', function (filePath) {
    let content = fs.readFileSync(filePath, 'utf8');
    content = content.replace(/bg-gradient-to-r from-slate-800 via-slate-900 to-slate-800/g, 'bg-blue-900');
    fs.writeFileSync(filePath, content, 'utf8');
});
walkDir('src/app/components/passeport-rh', function (filePath) {
    let content = fs.readFileSync(filePath, 'utf8');
    content = content.replace(/bg-gradient-to-r from-slate-800 via-slate-900 to-slate-800/g, 'bg-blue-900');
    fs.writeFileSync(filePath, content, 'utf8');
});

console.log('All HTML files themed.');
