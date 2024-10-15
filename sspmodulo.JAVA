package main

import (
    "crypto/aes"
    "crypto/cipher"
    "crypto/rand"
    "encoding/base64"
    "errors"
    "fmt"
    "io"
)

// SeaSecureProtocol: Módulo de Protocolo Seguro do Mar
type SeaSecureProtocol struct {
    symmetricKey []byte // Chave simétrica para criptografia
}

// NewSeaSecureProtocol: Inicializa o módulo com uma chave simétrica aleatória
func NewSeaSecureProtocol() (*SeaSecureProtocol, error) {
    key := make([]byte, 32) // 256 bits para AES-256
    if _, err := io.ReadFull(rand.Reader, key); err != nil {
        return nil, err
    }
    
    fmt.Println("SSP Module 3 initialized with a secure symmetric key.")
    return &SeaSecureProtocol{symmetricKey: key}, nil
}

// Encrypt: Função para criptografar dados (texto ou binário)
func (ssp *SeaSecureProtocol) Encrypt(data []byte) (string, error) {
    block, err := aes.NewCipher(ssp.symmetricKey)
    if err != nil {
        return "", fmt.Errorf("failed to create cipher: %w", err)
    }

    gcm, err := cipher.NewGCM(block)
    if err != nil {
        return "", fmt.Errorf("failed to create GCM: %w", err)
    }

    nonce := make([]byte, gcm.NonceSize())
    if _, err := io.ReadFull(rand.Reader, nonce); err != nil {
        return "", fmt.Errorf("failed to read nonce: %w", err)
    }

    ciphertext := gcm.Seal(nonce, nonce, data, nil)
    fmt.Println("Data successfully encrypted.")
    
    return base64.StdEncoding.EncodeToString(ciphertext), nil
}

// Decrypt: Função para descriptografar dados
func (ssp *SeaSecureProtocol) Decrypt(encryptedData string) ([]byte, error) {
    ciphertext, err := base64.StdEncoding.DecodeString(encryptedData)
    if err != nil {
        return nil, fmt.Errorf("failed to decode base64: %w", err)
    }

    block, err := aes.NewCipher(ssp.symmetricKey)
    if err != nil {
        return nil, fmt.Errorf("failed to create cipher: %w", err)
    }

    gcm, err := cipher.NewGCM(block)
    if err != nil {
        return nil, fmt.Errorf("failed to create GCM: %w", err)
    }

    nonceSize := gcm.NonceSize()
    if len(ciphertext) < nonceSize {
        return nil, errors.New("ciphertext too short")
    }

    nonce, ciphertext := ciphertext[:nonceSize], ciphertext[nonceSize:]
    decryptedData, err := gcm.Open(nil, nonce, ciphertext, nil)
    if err != nil {
        return nil, fmt.Errorf("decryption failed: %w", err)
    }

    return decryptedData, nil
}

// Exemplo de uso
func main() {
    ssp, err := NewSeaSecureProtocol()
    if err != nil {
        fmt.Println("Error initializing SeaSecureProtocol:", err)
        return
    }

    // Exemplo de dados a serem criptografados
    data := []byte("Hello, secure world!")

    // Criptografar dados
    encryptedData, err := ssp.Encrypt(data)
    if err != nil {
        fmt.Println("Error encrypting data:", err)
        return
    }
    fmt.Println("Encrypted Data:", encryptedData)

    // Descriptografar dados
    decryptedData, err := ssp.Decrypt(encryptedData)
    if err != nil {
        fmt.Println("Error decrypting data:", err)
        return
    }
    fmt.Println("Decrypted Data:", string(decryptedData))
}
