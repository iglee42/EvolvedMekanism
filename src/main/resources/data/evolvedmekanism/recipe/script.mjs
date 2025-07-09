import { readFileSync, writeFileSync, readdirSync, statSync } from 'fs';
import { join } from 'path';
import path from 'path';
import { fileURLToPath } from 'url';

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const rootDir = path.resolve(__dirname);

function transformObject(obj) {
  if (Array.isArray(obj)) {
    return obj.map(transformObject);
  } else if (typeof obj === 'object' && obj !== null) {
    const newObj = {};

    for (const key of Object.keys(obj)) {
      const value = obj[key];

      if (
        key === 'ingredient' &&
        typeof value === 'object' &&
        value !== null &&
        Object.keys(value).length === 1 &&
        'item' in value
      ) {
        newObj['item'] = value['item'];

      } else {
        newObj[key] = transformObject(value); // recurse
      }
    }

    return newObj;
  }

  return obj;
}

function processFile(filePath) {
  try {
    const content = readFileSync(filePath, 'utf8');
    const json = JSON.parse(content);
    const transformed = transformObject(json);

    if (JSON.stringify(json) !== JSON.stringify(transformed)) {
      writeFileSync(filePath, JSON.stringify(transformed, null, 2), 'utf8');
      console.log(`✅ Modifié : ${filePath}`);
    }
  } catch (err) {
    console.error(`❌ Erreur dans ${filePath}:`, err.message);
  }
}

function walkDirectory(dirPath) {
  const entries = readdirSync(dirPath);
  for (const entry of entries) {
    const fullPath = join(dirPath, entry);
    const stat = statSync(fullPath);
    if (stat.isDirectory()) {
      walkDirectory(fullPath);
    } else if (stat.isFile() && fullPath.endsWith('.json')) {
      processFile(fullPath);
    }
  }
}

walkDirectory(rootDir);
