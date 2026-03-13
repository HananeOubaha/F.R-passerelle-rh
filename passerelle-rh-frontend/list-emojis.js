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

const allEmojis = new Set();
const files = [];

walkDir('src/app', function (filePath) {
    files.push(filePath);
    let content = fs.readFileSync(filePath, 'utf8');
    // simple match for emojis using regex
    const matches = content.match(/[\p{Emoji_Presentation}\p{Extended_Pictographic}]/gu);
    if (matches) {
        matches.forEach(e => allEmojis.add(e));
    }
});

console.log(Array.from(allEmojis).join(' '));
