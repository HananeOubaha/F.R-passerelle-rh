const fs = require('fs');
const path = require('path');

function walkDir(dir, callback) {
    fs.readdirSync(dir).forEach(f => {
        let dirPath = path.join(dir, f);
        let isDirectory = fs.statSync(dirPath).isDirectory();
        if (isDirectory) {
            walkDir(dirPath, callback);
        } else if (dirPath.endsWith('.html')) {
            callback(dirPath);
        }
    });
}

const emojisToRemove = ['👥', '⚙️', '⚙', '🚀', '📝', '📅', '⚠️', '⚠', '📧', '📋', '🔗', '📜', '👤', '✅', '❌', '👋'];

walkDir('src/app', function (filePath) {
    let content = fs.readFileSync(filePath, 'utf8');

    emojisToRemove.forEach(emoji => {
        // Escape emoji if necessary, but these are fine to pass to RegExp. In fact string replace all logic:
        // replacing emoji followed by a space to avoid double spacing left over, and then just the emoji.
        let regexWithSpace = new RegExp(emoji + ' ', 'g');
        let regexJustEmoji = new RegExp(emoji, 'g');

        content = content.replace(regexWithSpace, '');
        content = content.replace(regexJustEmoji, '');
    });

    fs.writeFileSync(filePath, content, 'utf8');
});

console.log('Emojis removed.');
