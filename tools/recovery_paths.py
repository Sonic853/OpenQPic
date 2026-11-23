"""Locate the optional recovery materials for the sibling-repo or portable ZIP layout."""
import os
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
_candidates = (ROOT.parent / "OpenPic", ROOT.parent)
_configured = os.environ.get("OPENPIC_RECOVERY_ROOT")
RECOVERY_ROOT = ((ROOT / _configured).resolve() if _configured else
                 next((p for p in _candidates if (p / "reference/java").is_dir()), _candidates[0]))
