# EDT MCP Server

MCP (Model Context Protocol) server plugin for 1C:EDT, enabling AI assistants (Claude, GitHub Copilot, Cursor, etc.) to interact with EDT workspace.

## Features

- 🔧 **MCP Protocol 2025-11-25** - Streamable HTTP transport with SSE support
- 📊 **Project Information** - List workspace projects and configuration properties
- 🔴 **Error Reporting** - Get errors, warnings, problem summaries with filters
- 📝 **Check Descriptions** - Get check documentation from markdown files
- 🔄 **Project Revalidation** - Trigger revalidation when validation gets stuck
- 🔖 **Bookmarks & Tasks** - Access bookmarks and TODO/FIXME markers
- 🎯 **Status Bar** - Real-time server status indicator with request counter

## Installation

### From Update Site

1. In EDT: **Help → Install New Software...**
2. Add update site URL
3. Select **EDT MCP Server Feature**
4. Restart EDT

### Configuration

Go to **Window → Preferences → MCP Server**:
- **Server Port**: HTTP port (default: 8765)
- **Check descriptions folder**: Path to check description markdown files
- **Auto-start**: Start server on EDT launch

## Connecting AI Assistants

### VS Code / GitHub Copilot

Create `.vscode/mcp.json`:
```json
{
  "servers": {
    "EDT MCP Server": {
      "type": "sse",
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

### Cursor IDE

Create `.cursor/mcp.json`:
```json
{
  "mcpServers": {
    "EDT MCP Server": {
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

### Claude Desktop

Add to `claude_desktop_config.json`:
```json
{
  "mcpServers": {
    "EDT MCP Server": {
      "url": "http://localhost:8765/mcp"
    }
  }
}
```

## Available Tools

| Tool | Description |
|------|-------------|
| `get_edt_version` | Returns current EDT version |
| `list_projects` | Lists workspace projects with properties |
| `get_configuration_properties` | Gets 1C configuration properties |
| `get_project_errors` | Returns EDT problems with severity/checkId filters |
| `get_problem_summary` | Problem counts grouped by project and severity |
| `revalidate_project` | Triggers project revalidation |
| `get_bookmarks` | Returns workspace bookmarks |
| `get_tasks` | Returns TODO/FIXME task markers |
| `get_check_description` | Returns check documentation from .md files |

### Output Formats

- **Markdown tools**: `list_projects`, `get_project_errors`, `get_bookmarks`, `get_tasks`, `get_problem_summary`, `get_check_description` - return Markdown as EmbeddedResource with `mimeType: text/markdown`
- **JSON tools**: `get_configuration_properties`, `revalidate_project` - return JSON with `structuredContent`
- **Text tools**: `get_edt_version` - return plain text

## API Endpoints

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/mcp` | POST | MCP JSON-RPC (initialize, tools/list, tools/call) |
| `/mcp` | GET | Server info |
| `/health` | GET | Health check |

## Status Bar

Click the status indicator in EDT status bar:
- 🟢 Green - Server running
- ⚫ Grey - Server stopped
- **[N]** - Request counter

## Requirements

- 1C:EDT 2025.2 (Ruby) or later
- Java 17+

## Version History

### 1.5.1
- Dynamic file names for EmbeddedResource (e.g., `begin-transaction.md` instead of `tool-result`)
- Added `getResultFileName()` method to IMcpTool interface

### 1.5.0
- Explicit ResponseType per tool (TEXT, JSON, MARKDOWN)
- Markdown returned as EmbeddedResource with mimeType
- JSON returned with structuredContent support

### 1.4.0
- Converted list tools to Markdown output
- Fixed unused code warnings

### 1.3.0
- MCP Protocol 2025-11-25 with Streamable HTTP
- SSE transport support
- Session management with MCP-Session-Id header

### 1.2.0
- EDT IMarkerManager integration
- EDT severity levels (BLOCKER, CRITICAL, MAJOR, MINOR, TRIVIAL)

### 1.0.0
- Initial release

## License

Copyright (c) 2025 DitriX. All rights reserved.

---
*EDT MCP Server v1.5.1*
